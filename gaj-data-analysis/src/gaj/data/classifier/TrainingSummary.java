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
	 * Indicates the accuracy score of the classifier on the gold-standard data set before training.
	 * 
	 * @return The initial score.
	 */
	double initalScore();

	/**
	 * Indicates the accuracy score of the classifier on the gold-standard data set after training.
	 * 
	 * @return The final score.
	 */
	double finalScore();

}
