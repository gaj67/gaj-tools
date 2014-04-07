package gaj.data.classifier;

/**
 * Represents a single data point, comprising a feature
 * vector of data with a known classification.
 */
public interface GoldDatum {

	FeatureVector getFeatures();

	/**
	 * Specifies the index of the true class
	 * of the corresponding feature vector. 
	 * 
	 * @return
	 */
	int getClassIndex();
	
	/**
	 * Specifies the weight to attach to the feature vector.
	 * <p/>This could, for example, represent the number of
	 * times this particular vector of features occurred.
	 * 
	 * @return The feature weight.
	 */
	double getWeight();

}
