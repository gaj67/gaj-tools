package gaj.analysis.classifier;

import gaj.analysis.numeric.NumericFactory;
import gaj.data.classifier.DataScorer;
import gaj.data.classifier.DatumScore;
import gaj.data.classifier.TrainableClassifier;
import gaj.data.classifier.TrainingParams;
import gaj.data.classifier.TrainingSummary;
import gaj.data.numeric.DataObject;

public abstract class GradientAscentTrainer implements TrainableClassifier {

	/** Total number C of classes. */
	protected final int numClasses;
	/** Number F of features in a data vector. */
	protected final int numFeatures;

	protected GradientAscentTrainer(int numClasses, int numFeatures) {
		this.numClasses = numClasses;
		this.numFeatures = numFeatures;
	}

	@Override
	public int numClasses() {
		return numClasses;
	}

	@Override
	public int numFeatures() {
		return numFeatures;
	}

	@Override
	public TrainingSummary train(final TrainingParams params, DataScorer... scorers) {
		if (scorers.length == 0 || !scorers[0].hasGradient())
			throw new IllegalArgumentException("A training-data gradient scorer is required");
		double[] scores = new double[scorers.length];
		DataObject gradient = computeScoresAndGradient(scores, scorers);
		double[] newScores = new double[scorers.length];
		int numIterations = 0;
		double stepSize = 0.5;
		while (true) {
			if (NumericFactory.norm(gradient) <= params.gradientTolerance()) break;
			if (!updateParams(NumericFactory.scale(gradient, stepSize, false))) break;
			DataObject newGradient = computeScoresAndGradient(newScores, scorers);
			if (++numIterations >= params.maxIterations()) break;
			double diffScore = newScores[0] - scores[0];
			if (Math.abs(diffScore) <= params.scoreTolerance()) break;
			if (diffScore < 0) { // Over-shot target?
				stepSize = -0.5 * Math.abs(stepSize); // Attempt to correct on next iteration.
				continue;
			}
			if (terminate(scores, newScores)) break;
			stepSize = 0.5;
			double[] tmp = newScores;
			newScores = scores;
			scores = tmp;
			gradient = newGradient;
		}
		return null;
	}

	/**
	 * Attempts to update the classifier parameters by the 
	 * given increments. If the parameters do not change,
	 * then iterative training will cease.
	 * 
	 * @param deltaParams - The length-V vector of
	 * parameter value increments.
	 * @return A value of true (or false) if the parameters
	 * have (or have not) been updated.
	 */
	protected abstract boolean updateParams(DataObject deltaParams);

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

	private DataObject computeScoresAndGradient(double[] scores, DataScorer... scorers) {
		scores[0] = Double.NEGATIVE_INFINITY;
		double sumWeights = 0;
		DataObject sumGradient = null;
		for (DatumScore datumScore : scorers[0].scores(this)) {
			scores[0] = datumScore.getAverageScore();
			final double weight = datumScore.getWeight();
			sumWeights += weight;
			DataObject datumGradient = NumericFactory.scale(
					getGradient(datumScore),
					weight, false);
			sumGradient = (sumGradient == null) 
					? datumGradient
					: NumericFactory.add(sumGradient, datumGradient);
		}
		sumGradient = NumericFactory.scale(sumGradient, 1 / sumWeights, false);
		for (int i = 1; i < scorers.length; i++)
			scores[i] = scorers[i].score(this);
		return sumGradient;
	}

	/**
	 * Computes the gradient of the datum score with respect to the
	 * model parameters.
	 * 
	 * @param datumScore - The score of the feature vector, x.
	 * @return The score gradient.
	 */
	protected abstract DataObject getGradient(DatumScore datumScore);

}
