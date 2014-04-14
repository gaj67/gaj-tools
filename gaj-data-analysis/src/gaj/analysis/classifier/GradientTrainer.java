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
		double[] trainingScore = new double[1];
		DataVector gradient = computeScoreGradient(trainingScore, trainingScorer);
		double[] testingScores = new double[testingScorers.length];
		computeScores(testingScores, testingScorers);
		int numIterations = 0;
		return null;
	}

	private DataVector computeScoreGradient(double[] score, DataScorer scorer) {
		score[0] = Double.NEGATIVE_INFINITY;
		double sumWeights = 0;
		DataVector sumGradient = NumericDataFactory.newZeroVector(numParameters);
		for (DatumScore datumScore : scorer.scores(this)) {
			score[0] = datumScore.getAverageScore();
			final double weight = datumScore.getWeight();
			sumWeights += weight;
			DataVector scoreGradient = datumScore.getGradient();
			DataMatrix probsGradient = gradient(datumScore.getFeatures());
			DataVector datumGradient = NumericDataFactory.scale(
					NumericDataFactory.multiply(probsGradient, scoreGradient),
					weight);
			sumGradient = NumericDataFactory.add(sumGradient, datumGradient);
		}
		if (sumWeights > 0)
			sumGradient = NumericDataFactory.scale(sumGradient, 1 / sumWeights);
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
	protected abstract DataMatrix gradient(DataVector features);

	private void computeScores(double[] scores, DataScorer[] scorers) {
		for (int i = 0; i < scorers.length; i++)
			scores[i] = scorers[i].score(this);
	}

}
