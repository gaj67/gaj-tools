package gaj.analysis.classifier;

import gaj.analysis.data.vector.DataVector;

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
     * Computes the posterior distribution of a vector of features.
     * 
     * @param features - The length-F feature vector, x.
     * @return The classification object.
     */
    Classification classify(DataVector features);

}
