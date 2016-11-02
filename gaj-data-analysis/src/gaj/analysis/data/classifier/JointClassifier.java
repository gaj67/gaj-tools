package gaj.analysis.data.classifier;

import gaj.analysis.data.vector.DataVector;

/**
 * Specifies a probabilistic classifier of numerical data that uses a joint class/data model.
 */
public interface JointClassifier extends Classifier {

    /**
     * Computes the posterior and joint distributions of a vector of features.
     * 
     * @param features - The length-F feature vector, x.
     * @return The classification object.
     */
    @Override
    JointClassification classify(DataVector features);

}
