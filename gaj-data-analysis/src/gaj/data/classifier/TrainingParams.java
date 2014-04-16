package gaj.data.classifier;

/**
 * Specifies any parameter settings required for updating a trainable classifier.
 */
public interface TrainingParams {

	/**
	 * Specifies the maximum number of update iterations to perform during training.
	 * 
	 * @return The maximum number of iterations, or a negative value if there is no maximum.
	 */
	int maxIterations();
	
	/**
	 * Specifies the smallest absolute difference in accuracy scores between update iterations, 
	 * at or below which training will cease. 
	 * 
	 * @return The minimum score tolerance.
	 */
	double scoreTolerance();
	
	/**
	 * Specifies the smallest feasible norm of the score gradient, at or below which
	 * training will cease. 
	 * 
	 * @return The minimum score gradient tolerance.
	 */
	double gradientTolerance();

}
