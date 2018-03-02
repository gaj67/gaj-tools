package gaj.analysis.classifier.updated;

import gaj.analysis.data.numeric.vector.DataVector;

/**
 * Specifies a probabilistic classifier of numerical data that uses a joint class/data model.
 */
public interface JointClassifier extends DiscriminativeClassifier {

    /**
     * Computes the posterior and joint distributions of a vector of features.
     * 
     * @param features - The length-F feature vector, x.
     * @return The classification object.
     */
    @Override
    JointClassification classify(DataVector features);

}
