package gaj.analysis.classifier;

import gaj.data.classifier.Classifier;
import gaj.data.classifier.GoldData;
import gaj.data.classifier.GoldDataScorer;
import gaj.data.classifier.GoldDatum;
import gaj.data.classifier.GoldDatumScore;
import gaj.data.numeric.DataVector;

import java.util.Iterator;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Binds an arbitrary scorer to gold-standard data.
 * <p/>Assumes the overall accuracy score is the weighted average: 
 * <pre><tt>s = sum_{d=1}^{N} w_d * s_d / sum_{d=1}^{N} w_d</tt></pre>
 * where <tt>s_d</tt> is the unweighted score of the <tt>d</tt>-th data-point, computed from knowing
 * the posterior class probabilities P(c|x_d) for feature vector x_d, 
 * along with the true class index c_d. 
 */
/*package-private*/ abstract class WeightedAverageScorer implements GoldDataScorer {

	private final GoldData goldStandard;

	/**
	 * Computes the unweighted accuracy score of the given classification of feature vector x.
	 * 
	 * @param probs - The vector of posterior class probabilities, P(c|x).
	 * @param classIndex - The index of the true class for x.
	 * @return The unweighted score.
	 */
	protected abstract double score(DataVector probs, int classIndex);

	protected WeightedAverageScorer(GoldData goldStandard) {
		this.goldStandard = goldStandard;
	}

	@Override
	public GoldData getGoldData() {
		return goldStandard;
	}

	@Override
	public Iterable<GoldDatumScore> scores(final Classifier classifier) {
		return new Iterable<GoldDatumScore>() {
			private double sumWeights = 0;
			private double sumScores = 0;

			@Override
			public Iterator<GoldDatumScore> iterator() {
				return new Iterator<GoldDatumScore>() {
					private final Iterator<GoldDatum> iter = goldStandard.iterator();

					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public GoldDatumScore next() {
						final GoldDatum datum = iter.next();
						DataVector probs = classifier.classify(datum.getFeatures());
						final double unweightedScore = score(probs, datum.getClassIndex());
						final double weight = datum.getWeight(); 
						sumWeights += weight;
						sumScores += weight * unweightedScore;
						final double averageScore = 
								(sumWeights <= 0) ? Double.NEGATIVE_INFINITY 
										: (sumScores / sumWeights); 
						return new GoldDatumScore() {
							@Override
							public GoldDatum getGoldDatum() {
								return datum;
							}

							@Override
							public double getAverageScore() {
								return averageScore;
							}

							@Override
							public double getUnweightedScore() {
								return unweightedScore;
							}

							@Override
							public double getWeight() {
								return weight;
							}
						};
					}

					@Override
					public void remove() {
						throw new NotImplementedException();
					}
				};
			}
		};
	}

	@Override
	public double score(Classifier classifier) {
		double sumWeights = 0;
		double sumScores = 0;
		for (GoldDatum datum : goldStandard) {
			final double weight = datum.getWeight(); 
			sumWeights += weight;
			DataVector probs = classifier.classify(datum.getFeatures());
			final double unweightedScore = score(probs, datum.getClassIndex());
			sumScores += weight * unweightedScore;
		}
		return (sumWeights <= 0) ? Double.NEGATIVE_INFINITY : (sumScores / sumWeights); 
	}

}
