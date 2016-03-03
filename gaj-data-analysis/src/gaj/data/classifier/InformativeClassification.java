package gaj.data.classifier;

import gaj.data.vector.DataVector;

/**
 * Specifies the probabilistic, informative classification of a given instance of numerical feature data.
 */
public interface InformativeClassification extends JointClassification {

    /**
     * Specifies the distribution of the class priors.
     * 
     * @return A length-C vector of prior class probabilities, P(c).
     */
    DataVector priors();

    /**
     * Computes the conditional distribution of a vector of features.
     * 
     * @param features - The length-F feature vector, x.
     * @return A length-C vector of conditional probabilities, p(x|c).
     */
    DataVector likelihood();

}
