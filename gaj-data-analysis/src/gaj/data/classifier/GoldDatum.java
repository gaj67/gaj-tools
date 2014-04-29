package gaj.data.classifier;

import gaj.data.vector.DataVector;

/**
 * Represents a single data point, comprising a feature
 * vector of data with a known classification.
 * <p/>In addition, the data point has a specified weighting.
 * This could, for example, represent the number of
 * times this particular vector of feature values occurred.
 * Alternatively, it could be used to indicate ambiguity of the
 * underlying classification, e.g. the same feature vector 
 * is repeated in a number of data points with
 * different class indices and proportional weights that sum to 1.
 */
public interface GoldDatum {

	/**
	 * Obtains the numerical feature data.
	 * 
	 * @return A length-F vector of feature data.
	 */
	DataVector getFeatures();

	/**
	 * Specifies the index of the true class
	 * of the corresponding feature vector. 
	 * 
	 * @return The index of the true class.
	 */
	int getClassIndex();

	/**
	 * Specifies the weight to attach to the data point.
	 * 
	 * @return The feature weight.
	 */
	double getWeight();

}
