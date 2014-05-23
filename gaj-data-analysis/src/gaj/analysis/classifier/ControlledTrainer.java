package gaj.analysis.classifier;

import gaj.data.classifier.TrainingSummary;

/**
 * Specifies a single-user training algorithm for a ClassifierTrainer.
 */
public interface ControlledTrainer {

	/**
	 * Manually starts a training run from iteration 0.
	 */
	void start();

	/**
	 * Manually ends the training run, and summarises the training pocess.
	 * 
	 * @return A summary of the training process.
	 */
	TrainingSummary end();

	/**
	 * Performs at most one major iteration
	 * of the training process, with an update of the classifier parameters
	 * that attempts to improve the classification score.
	 * <p/>This method is responsible for setting
	 * the iteration count and computing the classifier scores, 
	 * and then testing these values against the training control parameters.
	 * 
	 * @return A value of true (or false) if 
	 * further training is (or is not) permitted.
	 */
	boolean update();

	/**
	 * Checks whether or not training should cease
	 * prior to an update iteration.
	 * 
	 * @return A value of true (or false) if training
	 * should (or should not) cease.
	 */
	boolean preTerminate();

	/**
	 * Checks whether or not iterative training should cease
	 * given the change in scores due to an update iteration. 
	 * For example, testing scores could be
	 * used to control over-training.
	 * 
	 * @param newScores - The classifier scores after a parameter update.
	 * @return A value of true (or false) if training
	 * should (or should not) cease.
	 */
	boolean postTerminate(double[] newScores);

}
