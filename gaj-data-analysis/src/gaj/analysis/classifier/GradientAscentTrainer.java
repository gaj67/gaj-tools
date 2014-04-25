package gaj.analysis.classifier;

import java.util.concurrent.atomic.AtomicInteger;

import gaj.analysis.numeric.NumericFactory;
import gaj.data.classifier.TrainableClassifier;
import gaj.data.classifier.DataScorer;
import gaj.data.classifier.DatumScore;
import gaj.data.classifier.TrainingParams;
import gaj.data.classifier.TrainingSummary;
import gaj.data.classifier.UpdatableClassifier;
import gaj.data.numeric.DataObject;

public abstract class GradientAscentTrainer<T extends DataObject> implements TrainableClassifier {

	protected final UpdatableClassifier<T> classifier;

	protected GradientAscentTrainer(UpdatableClassifier<T> classifier) {
		if (!classifier.hasGradient())
			throw new IllegalArgumentException("Gradient ascent requires getGradient()");
		this.classifier = classifier;
	}

	@Override
	public int numClasses() {
		return classifier.numClasses();
	}

	@Override
	public int numFeatures() {
		return classifier.numFeatures();
	}

	@Override
	public TrainingSummary train(final TrainingParams control, DataScorer... scorers) {
		if (scorers.length == 0 || !scorers[0].hasGradient())
			throw new IllegalArgumentException("A training-data gradient scorer is required");
		double[] scores = new double[scorers.length];
		DataObject gradient = computeScoresAndGradient(scores, scorers);
		double[] newScores = new double[scorers.length];
		DataObject newGradient = null;
		AtomicInteger numIterations = new AtomicInteger();
		double[] stepSize = new double[] { 0.5 };
		outer: while (true) {
			if (NumericFactory.norm(gradient) <= control.gradientTolerance()) break;
			inner: while (true) {
				if (!updateParams(NumericFactory.scale(gradient, stepSize[0]))) 
					break outer;
				newGradient = computeScoresAndGradient(newScores, scorers);
				if (numIterations.incrementAndGet() >= control.maxIterations()) 
					break outer;
				double diffScore = newScores[0] - scores[0];
				if (Math.abs(diffScore) <= control.scoreTolerance()) 
					break outer;
				if (diffScore > 0) break inner;
				reduceStepSize(stepSize);
			}
			if (terminate(scores, newScores)) break;
			gradient = updateGradientAndStepSize(stepSize, scores[0], gradient, newScores[0], newGradient);
			double[] tmp = newScores;
			newScores = scores;
			scores = tmp;
		}
		return null;
	}

	private DataObject computeScoresAndGradient(double[] scores, DataScorer... scorers) {
		scores[0] = Double.NEGATIVE_INFINITY;
		double sumWeights = 0;
		DataObject sumGradient = null;
		for (DatumScore datumScore : scorers[0].getScores(this)) {
			scores[0] = datumScore.getAverageScore();
			final double weight = datumScore.getWeight();
			sumWeights += weight;
			DataObject datumGradient = NumericFactory.scale(
					classifier.getGradient(datumScore),
					weight);
			sumGradient = (sumGradient == null) 
					? datumGradient
					: NumericFactory.add(sumGradient, datumGradient);
		}
		sumGradient = NumericFactory.scale(sumGradient, 1 / sumWeights);
		for (int i = 1; i < scorers.length; i++)
			scores[i] = scorers[i].getScore(this);
		return sumGradient;
	}

	@SuppressWarnings("unchecked")
	private boolean updateParams(DataObject increment) {
		DataObject newParams = NumericFactory.add(classifier.getParameters(), increment);
		return classifier.setParameters((T) newParams);
	}

	/**
	 * Checks whether or not iterative training should cease
	 * given the change in scores. For example, testing scores could be
	 * used to control over-training.
	 * 
	 * @param prevScores - The classifier scores prior to the parameter update.
	 * @param newScores - The classifier scores after the update.
	 * @return A value of true (or false) if training
	 * should (or should not) cease.
	 */
	protected boolean terminate(double[] prevScores, double[] newScores) {
		// TODO Check if avg. testing score has decreased.
		return false;
	}

	/**
	 * Reduces the step-size if over-shooting has occurred,
	 * i.e. the local score has gone down instead of up.
	 * <p/>Note: In this implementation the original gradient
	 * has been kept, but not the model parameter values.
	 * Hence, the step-size must be made negative to
	 * reverse the over-shooting.
	 * 
	 * @param stepSize - The step-size, stored in a length-1 array.
	 */
	protected void reduceStepSize(double[] stepSize) {
		stepSize[0] = -0.5 * Math.abs(stepSize[0]);
	}

	/**
	 * Calculates an improved estimate of the 
	 * gradient and a corresponding step-size.
	 * 
	 * @param stepSize - The step-size, stored in a length-1 array.
	 * @param score - The initial score.
	 * @param gradient - The inital gradient.
	 * @param newScore - The new score.
	 * @param newGradient - The new gradient.
	 * @return An updated gradient estimate.
	 */
	protected DataObject updateGradientAndStepSize(
			double[] stepSize,
			double score, DataObject gradient, 
			double newScore, DataObject newGradient) {
		// TODO Use quadratic acceleration.
		// TODO Use cubic spline acceleration.
		stepSize[0] = 0.5;
		return newGradient;
	}

}
