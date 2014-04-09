package gaj.data.classifier;

import gaj.data.numeric.DataVector;

import java.util.Iterator;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Binds an arbitrary score to gold-standard data.
 */
/*package-private*/ abstract class AbstractScorer implements GoldDataScorer {

	private final GoldData goldStandard;

	/**
	 * Computes the accuracy score of the given classification of feature vector x.
	 * 
	 * @param probs - The vector of posterior class probabilities, P(c|x).
	 * @param classIndex - The index of the true class for x.
	 * @return The accuracy score.
	 */
	protected abstract double score(DataVector probs, int classIndex);

	protected AbstractScorer(GoldData goldStandard) {
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
