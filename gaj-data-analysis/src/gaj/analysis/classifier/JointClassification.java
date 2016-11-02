package gaj.analysis.classifier;

import gaj.analysis.data.vector.DataVector;

/**
 * Specifies the probabilistic, joint classification of a given instance of numerical feature data.
 */
public interface JointClassification extends Classification {

    /**
     * Obtains the joint distribution of the feature vector.
     * 
     * @return A length-C vector of joint class/data probabilities, p(c,x).
     */
    DataVector joint();

    /**
     * Obtains the marginal likelihood of the feature vector.
     * 
     * @return The marginal likelihood, p(x).
     */
    double marginal(DataVector features);

}
