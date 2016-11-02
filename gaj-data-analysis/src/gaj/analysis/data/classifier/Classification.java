package gaj.analysis.data.classifier;

import gaj.analysis.data.vector.DataVector;

/**
 * Specifies the probabilistic, discriminative classification of a given instance of numerical feature data.
 */
public interface Classification {

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
     * Obtains the instance of features under classification.
     * 
     * @return The length-F feature vector.
     */
    DataVector features();

    /**
     * Obtains the posterior class distribution of the feature vector under classification.
     * 
     * @return A length-C vector of posterior class probabilities, P(c|x).
     */
    DataVector posteriors();

}
