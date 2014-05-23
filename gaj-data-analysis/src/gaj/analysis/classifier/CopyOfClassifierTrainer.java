package gaj.analysis.classifier;

import gaj.data.classifier.DataScorer;
import gaj.data.classifier.ScoredTrainer;
import gaj.data.classifier.TrainingControl;
import gaj.data.classifier.TrainingSummary;
import gaj.data.classifier.ParameterisedClassifier;

import java.util.Arrays;

/**
 * A base class for implementing a trainer for
 * a trainable classifier. The trainer is initialised
 * once, but may be repeatedly used (on the same data) by instantiating
 * the specified training algorithm class.
 */
public abstract class CopyOfClassifierTrainer implements ScoredTrainer {

	private final ParameterisedClassifier classifier;
	private final DataScorer[] scorers;
	private final Class<? extends TrainingAlgorithm> trainer;
	/**
	 * Current number of iterations during or after training.
	 */
	private int numIterations = 0;
	/**
	 * Current training and testing scores.
	 */
	private double[] scores;
	/**
	 * Copy of training and testing scores just prior to training.
	 */
	private double[] initialScores;

	/**
	 * Binds the classifier and scorers to the  training algorithm.
	 * 
	 * @param classifier - The classifier to be trained.
	 * @param scorers - The scorers to be evaluated.
	 * @param trainer - The training algorithm.
	 */
	protected CopyOfClassifierTrainer(ParameterisedClassifier classifier, DataScorer[] scorers, Class<? extends TrainingAlgorithm> trainer) {
		this.classifier = classifier;
		this.scorers = scorers;
		this.trainer = trainer;
	}

	@Override
	public int numIterations() {
		return numIterations;
	}

	@Override
	public double[] getScores() {
		return Arrays.copyOf(scores, scores.length);
	}

	@Override
	public TrainingSummary train(TrainingControl control) {
		start(control);
		while (iterate(control));
		return end(control);
	}

	/**
	 * Manually starts a training run, including
	 * initialising the iteration counter.
	 * 
	 * @param control - The control parameters.
	 */
	public void start(TrainingControl control);

	/**
	 * Manually ends the training run, and summarises the training pocess.
	 * 
	 * @param control - The control parameters.
	 * @return A summary of the training process.
	 */
	public TrainingSummary end(TrainingControl control);

	/**
	 * Performs at most one major iteration
	 * of the training process. 
	 * <p/>This method is responsible for setting
	 * the iteration count and computing the classifier scores, 
	 * and then testing these values against the training control parameters.
	 * 
	 * @param control - The control parameters.
	 * @return A value of true (or false) if 
	 * further training is (or is not) permitted.
	 */
	public boolean iterate(TrainingControl control);


	@Override
	public void start(TrainingControl control) {
		numIterations = 0;
		initialScores = Arrays.copyOf(scores, scores.length);
	}

	@Override
	public TrainingSummary end(TrainingControl control) {
		return new TrainingSummary() {
			private final int _numIterations = numIterations;
			private final double[] _initialScores = initialScores;
			private final double[] _finalScores = Arrays.copyOf(scores, scores.length);

			@Override
			public int numIterations() {
				return _numIterations;
			}

			@Override
			public double[] initalScores() {
				return _initialScores;
			}

			@Override
			public double[] finalScores() {
				return _finalScores;
			}
		};
	}

	@Override
	public boolean iterate(TrainingControl control) {
		if (preTerminate(control)) return false;
		double[] newScores = update(control);
		numIterations++;
		boolean halt = postTerminate(control, newScores);
		scores = newScores;
		return !halt;
	}

	/**
	 * Performs an update of the classifier parameters.
	 * 
	 * @param control - The control parameters.
	 * @return The updated classifier scores.
	 */
	protected abstract double[] update(TrainingControl control);

	/**
	 * Checks whether or not training should cease
	 * prior to an update iteration.
	 * 
	 * @param control - The control parameters.
	 * @return A value of true (or false) if training
	 * should (or should not) cease.
	 */
	protected boolean preTerminate(TrainingControl control) {
		return (control.maxIterations() > 0 && numIterations >= control.maxIterations());
	}

	/**
	 * Checks whether or not iterative training should cease
	 * given the change in scores due to an update iteration. 
	 * For example, testing scores could be
	 * used to control over-training.
	 * 
	 * @param control - The control parameters.
	 * @param newScores - The classifier scores after the update.
	 * @return A value of true (or false) if training
	 * should (or should not) cease.
	 */
	protected boolean postTerminate(TrainingControl control, double[] newScores) {
		// TODO Check if avg. testing score has decreased.
		if (control.scoreTolerance() > 0
				&& newScores[0] - scores[0] < control.scoreTolerance())
			return true;
		return (control.relativeScoreTolerance() > 0
				&& newScores[0] - scores[0] < Math.abs(scores[0]) * control.relativeScoreTolerance());
	}

}
