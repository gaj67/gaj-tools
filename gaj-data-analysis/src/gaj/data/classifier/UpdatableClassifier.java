package gaj.data.classifier;

import gaj.data.numeric.DataObject;

/**
 * Specifies a classifier that may have its parameters
 * updated.
 */
public interface UpdatableClassifier extends Classifier {

	/**
	 * Obtains a representation of the current
	 * classifier parameters.
	 * 
	 * @return The parameter values.
	 */
	DataObject getParameters();
	
	/**
	 * Updates the classifier parameters.
	 * 
	 * @param params - The new parameter values.
	 * @return A value of true (or false) if the 
	 * classifier parameters actually
	 * have (or have not) been updated.
	 */
	boolean setParameters(DataObject params);

	/**
	 * Indicates whether or not it is safe to call the
	 * {@link #getGradient}() method.
	 * 
	 * @return A value of true (or false) if the
	 * gradient can (or cannot) be computed.
	 */
	boolean hasGradient();

	/**
	 * Computes the gradient of the datum score 
	 * with respect to the classifier parameters.
	 * 
	 * @param datumScore - Specifies the score 
	 * of the current feature datum.
	 * @return The datum score gradient.
	 * @throws RuntimeException If the gradient
	 * cannot be computed.
	 */
	DataObject getGradient(DatumScore datumScore);

}
