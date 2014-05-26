package gaj.data.classifier;

/**
 * Summarises the iterative updates performed during classifier training.
 */
public interface TrainingSummary {

	/**
	 * Indicates the number of update iterations actually performed during training.
	 * 
	 * @return The number of iterations.
	 */
	int numIterations();

	/**
	 * Indicates the accuracy score(s) of the classifier on the gold-standard data before training.
	 * 
	 * @return The initial score(s).
	 */
	double[] initalScores();

	/**
	 * Indicates the accuracy score(s) of the classifier on the gold-standard data after training.
	 * 
	 * @return The final score(s).
	 */
	double[] finalScores();

	/**
	 * Indicates the reason why the training process halted.
	 * 
	 * @return The termination state.
	 */
	TrainingState getTrainingState();

}
