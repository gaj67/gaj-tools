package gaj.analysis.classifier;

import gaj.data.classifier.DataScorer;
import gaj.data.classifier.TrainingParams;
import gaj.data.classifier.TrainingSummary;
import gaj.data.classifier.UpdatableClassifier;

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
	protected int numIterations;

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
		initialise();
		isBound = true;
	}

	/**
	 * Initialises any required training properties.
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
		numIterations = 0;
		while (true) {
			if (!iterate(control)) break;
		}
		return getSummary();
	}

	/**
	 * Performs at most one major iteration
	 * of the training process 
	 * according to the given control parameters.
	 * This method is responsible for testing
	 * and incrementing the iteration count,
	 * as well as testing score tolerances, etc.
	 * 
	 * @param control - The control parameters.
	 * @return A value of true (or false) if 
	 * further training is (or is not) permitted.
	 */
	public abstract boolean iterate(TrainingParams control);

	/**
	 * Obtains the current status of the training process.
	 * 
	 * @return The summary data.
	 */
	public abstract TrainingSummary getSummary();

}
