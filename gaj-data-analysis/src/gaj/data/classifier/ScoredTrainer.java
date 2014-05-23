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
	public TrainingSummary train(TrainingControl control);

	/**
	 * Obtains the total number of iterations that have been performed by the trainer.
	 * <p/>This may include minor iterations as well as major iterations.
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
