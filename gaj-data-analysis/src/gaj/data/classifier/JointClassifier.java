package gaj.data.classifier;

import gaj.data.vector.DataVector;

/**
 * Specifies a probabilistic classifier of numerical data that uses a joint class/data model.
 */
public interface JointClassifier extends Classifier {

    /**
     * Computes the joint distribution of a vector of features.
     * 
     * @param features - The length-F feature vector, x.
     * @return A length-C vector of joint class/data probabilities, p(c,x).
     */
    DataVector joint(DataVector features);

    /**
     * Computes the marginal likelihood of a vector of features.
     * 
     * @param features - The length-F feature vector, x.
     * @return The marginal likelihood, p(x).
     */
    double marginal(DataVector features);

}
