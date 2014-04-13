package gaj.analysis.classifier;

import gaj.data.classifier.Classifier;
import gaj.data.classifier.DataGradientScorer;
import gaj.data.classifier.DatumGradientScore;
import gaj.data.classifier.GoldData;
import gaj.data.classifier.DataScorer;
import gaj.data.classifier.GoldDatum;
import gaj.data.classifier.DatumScore;
import gaj.data.numeric.DataVector;

import java.util.Iterator;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Binds an arbitrary scorer to gold-standard data, allowing
 * it to compute the individual score and gradient for each
 * data-point.
 */
public abstract class GradientScorer extends AverageScorer implements DataGradientScorer {

	/**
	 * Computes the gradient of the unweighted accuracy score 
	 * of the given classification of feature vector x.
	 * 
	 * @param probs - The vector of posterior class probabilities, P(c|x).
	 * @param classIndex - The index of the true class for x.
	 * @return The unweighted score gradient, ds/dP(c|x).
	 */
	protected abstract DataVector gradient(DataVector probs, int classIndex);

	/**
	 * Binds the given data to the scorer.
	 * 
	 * @param data - The gold-standard data set.
	 */
	protected GradientScorer(GoldData data) {
		super(data);
	}

	@Override
	public Iterable<? extends DatumGradientScore> scores(final Classifier classifier) {
		return new Iterable<DatumGradientScore>() {
			private double sumWeights = 0;
			private double sumScores = 0;

			@Override
			public Iterator<DatumGradientScore> iterator() {
				return new Iterator<DatumGradientScore>() {
					private final Iterator<GoldDatum> iter = getGoldData().iterator();

					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public DatumGradientScore next() {
						final GoldDatum datum = iter.next();
						final double weight = datum.getWeight(); 
						sumWeights += weight;
						DataVector probs = classifier.classify(datum.getFeatures());
						final double score = weight * score(probs, datum.getClassIndex());
						sumScores += score;
						final double averageScore = 
								(sumWeights <= 0) ? Double.NEGATIVE_INFINITY 
										: (sumScores / sumWeights);
						final DataVector gradient = gradient(probs, datum.getClassIndex());
						return new DatumGradientScore() {
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
							public DataVector getGradient() {
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

}
