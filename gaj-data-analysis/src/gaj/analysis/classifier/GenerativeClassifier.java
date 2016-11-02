package gaj.analysis.classifier;

import gaj.analysis.numeric.vector.DataVector;

/**
 * Specifies a probabilistic classifier of numerical data that uses a generative class/data model.
 */
public interface GenerativeClassifier extends JointClassifier {

    /**
     * Specifies the distribution of the class priors.
     * 
     * @return A length-C vector of prior class probabilities, P(c).
     */
    DataVector priors();

    /**
     * Computes the posterior, joint and likelihood distributions of a vector of features.
     * 
     * @param features - The length-F feature vector, x.
     * @return The classification object.
     */
    @Override
    InformativeClassification classify(DataVector features);

}
