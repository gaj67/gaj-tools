package afl.classifier;

import afl.features.FeatureMatrix;
import afl.features.FeatureVector;

public interface Classifier {

    /**
     * @return The number of classes.
     */
    int numClasses();

    /**
     * @param features - A 1xF vector of features.
     * @return A 1xC array of class-posterior probabilities.
     */
    double[] posteriors(FeatureVector features);

    /**
     * @param features - A 1xF vector of features.
     * @param class - The true class label in {0,1,...,C-1}.
     * @return The statistics obtained from measuring the success or otherwise of classifying the features.
     */
    Stats statistics(FeatureVector features, int klass);

    /**
     * Performs one update of the classifier parameters
     * from the supplied training data.
     * 
     * @param features - An NxF matrix of features.
     * @param classes - An Nx1 vector of class labels in {0,1,...,C-1}.
     * @return The score of the training data using the parameters prior to the update.
     */
    double update(FeatureMatrix features, int[] classes);

    /**
     * @param features - An NxF matrix of features.
     * @param classes - An Nx1 vector of class labels in {0,1,...,C-1}.
     * @return The statistics obtained from measuring the success or otherwise of classifying
     * the features.
     */
    Stats statistics(FeatureMatrix features, int[] classes);

    /**
     * Resets the model parameters to their default values.
     */
    void resetParameters();

    /**
     * @return The model parameters in the form of a vector.
     */
    FeatureVector getParameters();

    /**
     * Sets the model parameters from the supplied vector.
     * 
     * @param params The parameters obtained from getParameters().
     */
    void setParameters(FeatureVector params);

}
