package gaj.analysis.classifier;

import gaj.data.classifier.Classifier;
import gaj.data.classifier.GoldData;
import gaj.data.classifier.DataScorer;
import gaj.data.classifier.GoldDatum;
import gaj.data.classifier.DatumScore;
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
 * <p/>Also, by default, no gradient or other information 
 * is computed. If this is required, override {@link #hasGradient}()
 * and {@link #gradient}().
 */
public abstract class AverageScorer implements DataScorer {

	private final GoldData data;

	/**
	 * Computes the unweighted accuracy score of the given classification of feature vector x.
	 * 
	 * @param probs - The vector of posterior class probabilities, P(c|x).
	 * @param classIndex - The index of the true class for x.
	 * @return The unweighted score.
	 */
	protected abstract double score(DataVector probs, int classIndex);

	@Override
	public boolean hasGradient() {
		return false;
	}

	protected DataVector gradient(DataVector probs, int classIndex) {
		throw new NotImplementedException();
	}

	/**
	 * Binds the given data to the scorer.
	 * 
	 * @param data - The gold standard data set.
	 */
	protected AverageScorer(GoldData data) {
		this.data = data;
	}

	@Override
	public GoldData getGoldData() {
		return data;
	}

	@Override
	public int numClasses() {
		return data.numClasses();
	}

	@Override
	public int numFeatures() {
		return data.numFeatures();
	}

	@Override
	public Iterable<? extends DatumScore> scores(final Classifier classifier) {
		return new Iterable<DatumScore>() {
			private double sumWeights = 0;
			private double sumScores = 0;

			@Override
			public Iterator<DatumScore> iterator() {
				return new Iterator<DatumScore>() {
					private final Iterator<GoldDatum> iter = data.iterator();

					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public DatumScore next() {
						final GoldDatum datum = iter.next();
						final double weight = datum.getWeight(); 
						sumWeights += weight;
						final DataVector probs = classifier.classify(datum.getFeatures());
						final double score = weight * score(probs, datum.getClassIndex());
						sumScores += score;
						final double averageScore = 
								(sumWeights <= 0) ? Double.NEGATIVE_INFINITY 
										: (sumScores / sumWeights); 
						return new DatumScore() {
							private /*@Nullable*/ DataVector gradient = null;

							@Override
							public GoldDatum getGoldDatum() {
								return datum;
							}

							@Override
							public double getAverageScore() {
								return averageScore;
							}

							@Override
							public double getScore() {
								return score;
							}

							@Override
							public double getWeight() {
								return weight;
							}

							@Override
							public boolean hasGradient() {
								return AverageScorer.this.hasGradient();
							}

							@Override
							public DataVector getGradient() {
								if (gradient == null)
									gradient = gradient(probs, datum.getClassIndex());
								return gradient;
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
		for (GoldDatum datum : data) {
			final double weight = datum.getWeight(); 
			sumWeights += weight;
			DataVector probs = classifier.classify(datum.getFeatures());
			final double unweightedScore = score(probs, datum.getClassIndex());
			sumScores += weight * unweightedScore;
		}
		return (sumWeights <= 0) ? Double.NEGATIVE_INFINITY : (sumScores / sumWeights); 
	}

}
