package gaj.analysis.classifier;

import gaj.data.classifier.DataScorer;
import gaj.data.classifier.TrainingParams;
import gaj.data.classifier.TrainingSummary;
import gaj.data.classifier.UpdatableClassifier;

import java.util.Arrays;

/**
 * A base class for implementing a trainer for
 * a trainable classifier. The trainer is initialised
 * once, but may be repeatedly trained 
 * (on the same data).
 */
public abstract class ClassifierTrainer {

	private boolean isBound = false;
	protected UpdatableClassifier classifier;
	protected DataScorer[] scorers;
	/**
	 * Current number of iterations during or after training.
	 */
	protected int numIterations;
	/**
	 * Current training and testing scores.
	 */
	protected double[] scores;
	/**
	 * Copy of training and testing scores just prior to training.
	 */
	private double[] initialScores;

	/**
	 * Allows late binding of the arguments.
	 */
	protected ClassifierTrainer() {}

	/**
	 * Binds the trainer to the given arguments.
	 * 
	 * @param classifier - An updatable classifier.
	 * @param scorers - One or more data scorers.
	 */
	protected ClassifierTrainer(UpdatableClassifier classifier, DataScorer... scorers) {
		bindArguments(classifier, scorers);
	}

	/*package-private*/ final void bindArguments(UpdatableClassifier classifier, DataScorer[] scorers) {
		if (isBound)
			throw new IllegalStateException("The trainer is already in use");
		this.classifier = classifier;
		this.scorers = scorers;
		scores = new double[scorers.length];
		Arrays.fill(scores, Double.NEGATIVE_INFINITY);
		initialise();
		initialScores = Arrays.copyOf(scores, scores.length);
		isBound = true;
	}

	/**
	 * Initialises any required training properties,
	 * including the classifier scores.
	 */
	protected abstract void initialise();

	/**
	 * Updates the classifier parameters according
	 * to the given control parameters.
	 * The iterations are (re)started from zero.
	 * 
	 * @param control - The control parameters.
	 * @return A summary of the training process.
	 */
	public TrainingSummary train(TrainingParams control) {
		start();
		while (iterate(control));
		end();
		return getSummary();
	}

	/**
	 * Marks the start of a training run, including
	 * (re)initialising the iteration counter.
	 */
	protected void start() {
		numIterations = 0;
		initialScores = Arrays.copyOf(scores, scores.length);
	}

	/**
	 * Marks the end of a training run.
	 */
	protected void end() {
	}

	/**
	 * Performs at most one major iteration
	 * of the training process 
	 * according to the given control parameters.
	 * This method is responsible for testing
	 * the iteration count and score tolerances, etc.
	 * 
	 * @param control - The control parameters.
	 * @return A value of true (or false) if 
	 * further training is (or is not) permitted.
	 */
	public boolean iterate(TrainingParams control) {
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
	protected abstract double[] update(TrainingParams control);

	/**
	 * Checks whether or not training should cease
	 * prior to an update iteration.
	 * 
	 * @param control - The control parameters.
	 * @return A value of true (or false) if training
	 * should (or should not) cease.
	 */
	protected boolean preTerminate(TrainingParams control) {
		return (numIterations >= control.maxIterations());
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
	protected boolean postTerminate(TrainingParams control, double[] newScores) {
		// TODO Check if avg. testing score has decreased.
		return (control.scoreTolerance() > 0
				&& newScores[0] - scores[0] < control.scoreTolerance()); 
	}

	/**
	 * Obtains the current status of the training process.
	 * 
	 * @return The summary data.
	 */
	public TrainingSummary getSummary() {
		return new TrainingSummary() {
			private final int _numIterations = numIterations;
			private final double[] _initialScores = Arrays.copyOf(initialScores, initialScores.length);
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

}
