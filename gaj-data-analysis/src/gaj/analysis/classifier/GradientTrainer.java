package gaj.analysis.classifier;

import gaj.data.classifier.DataScorer;
import gaj.data.classifier.DatumScore;
import gaj.data.classifier.TrainableClassifier;
import gaj.data.classifier.TrainingParams;
import gaj.data.classifier.TrainingSummary;
import gaj.data.numeric.DataMatrix;
import gaj.data.numeric.DataVector;
import gaj.data.numeric.NumericDataFactory;

public abstract class GradientTrainer implements TrainableClassifier {

	/** Total number C of classes. */
	protected final int numClasses;
	/** Number F of features in a data vector. */
	protected final int numFeatures;
	/** Number V of parameters in the model. */
	protected final int numParameters;

	protected GradientTrainer(int numClasses, int numFeatures, int numParameters) {
		this.numClasses = numClasses;
		this.numFeatures = numFeatures;
		this.numParameters = numParameters;
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
	public TrainingSummary train(TrainingParams params,
			DataScorer trainingScorer, DataScorer... testingScorers) {
		if (!trainingScorer.hasGradient())
			throw new IllegalArgumentException("Gradient trainer requires gradient scorer");
		double[] scores = new double[testingScorers.length + 1];
		DataVector gradient = computeScoresAndGradient(scores, trainingScorer, testingScorers);
		final double scoreTolerance = params.scoreTolerance();
		final int maxIterations = params.maxIterations();
		int numIterations = 0;
		double[] newScores = new double[testingScorers.length + 1];
		double stepSize = 0.5;
		while (true) {
			if (!update(NumericDataFactory.scale(gradient, stepSize))) break;
			if (++numIterations >= maxIterations) break;
			gradient = computeScoresAndGradient(newScores, trainingScorer, testingScorers);
			double diffScore = newScores[0] - scores[0];
			if (diffScore < 0) { // Over-shot target?
				stepSize *= 0.5; // Attempt to correct on next iteration.
				continue;
			}
			if (diffScore <= scoreTolerance) break;
			if (terminate(scores, newScores)) break;
			stepSize = 0.5;
			double[] tmp = scores;
			scores = newScores;
			newScores = tmp;
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
	protected abstract boolean update(DataVector deltaParams);

	/**
	 * Checks whether or not iterative training should cease
	 * given the change in scores.
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

	private DataVector computeScoresAndGradient(double[] scores, DataScorer trainingScorer, DataScorer... testingScorers) {
		scores[0] = Double.NEGATIVE_INFINITY;
		double sumWeights = 0;
		DataVector sumGradient = NumericDataFactory.newZeroVector(numParameters);
		for (DatumScore datumScore : trainingScorer.scores(this)) {
			scores[0] = datumScore.getAverageScore();
			final double weight = datumScore.getWeight();
			sumWeights += weight;
			DataVector scoreGradient = datumScore.getGradient();
			DataMatrix probsGradient = getGradient(datumScore.getFeatures());
			DataVector datumGradient = NumericDataFactory.scale(
					NumericDataFactory.multiply(probsGradient, scoreGradient),
					weight);
			sumGradient = NumericDataFactory.add(sumGradient, datumGradient);
		}
		if (sumWeights > 0)
			sumGradient = NumericDataFactory.scale(sumGradient, 1 / sumWeights);
		for (int i = 0; i < testingScorers.length; i++)
			scores[i+1] = testingScorers[i].score(this);
		return sumGradient;
	}

	/**
	 * Computes the matrix derivative, <tt>dP(c_j|x)/dv_i</tt>,
	 * of posterior class probabilities with respect to the
	 * model parameters,
	 * where <tt>c_j</tt> is the <tt>j</tt>-th class 
	 * (of C classes)
	 * and <tt>v_i</tt> is the <tt>i</tt>-th parameter 
	 * (of V parameters).
	 * 
	 * @param features - The feature vector, x.
	 * @return The V x C matrix of derivatives.
	 */
	protected abstract DataMatrix getGradient(DataVector features);

}
