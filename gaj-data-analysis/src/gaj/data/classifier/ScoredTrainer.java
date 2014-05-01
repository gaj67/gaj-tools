package gaj.data.classifier;


/**
 * An interface for training a classifier on one or more pre-determined sets of data
 * with pre-determined scorers.
 */
public interface ScoredTrainer {

	/**
	 * Iteratively updates the classifier parameters according
	 * to the given control parameters. The iteration count is started from zero.
	 * 
	 * @param control - The control parameters.
	 * @return A summary of the training process.
	 */
	public TrainingSummary train(TrainingParams control);

	/**
	 * Manually starts a training run, including
	 * initialising the iteration counter.
	 * 
	 * @param control - The control parameters.
	 */
	public void start(TrainingParams control);

	/**
	 * Manually ends the training run, and summarises the training pocess.
	 * 
	 * @param control - The control parameters.
	 * @return A summary of the training process.
	 */
	public TrainingSummary end(TrainingParams control);

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
	public boolean iterate(TrainingParams control);

	/**
	 * Obtains the number of iterations that have been performed during training.
	 * 
	 * @return The number of training iterations.
	 */
	public int numIterations();

	/**
	 * Obtains a copy of the current classifier scores on the pre-determined data sets.
	 * 
	 * @return The classifier scores.
	 */
	public double[] getScores();

}
