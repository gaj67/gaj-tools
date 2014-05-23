package gaj.data.classifier;

import gaj.data.numeric.DataObject;
import gaj.data.vector.DataVector;


/**
 * Specifies a probabilistic classifier of numerical data.
 */
public interface Classifier {

	/**
	 * Indicates the total number C of classes in the classification scheme.
	 * 
	 * @return The number of classes.
	 */
	int numClasses();

	/**
	 * Indicates the number F of numerical features in a feature vector
	 * to be classified.
	 * 
	 * @return The number of feature elements.
	 */
	int numFeatures();

	/**
	 * Probabilistically classifies a vector of features.
	 * 
	 * @param features - The length-F feature vector, x.
	 * @return A length-C vector of posterior class probabilities, P(c|x).
	 */
	DataVector classify(DataVector features);

}
