package gaj.data.classifier;

import gaj.data.vector.DataVector;

/**
 * Specifies a classifier with a model controlled by parameters.
 * <p/>
 * Note: These parameters need not take the form of a single vector, but must be representable as a vector for external manipulation, e.g. parameter search for score optimisation.
 */
public interface ParameterisedClassifier extends Classifier {

    /**
     * Obtains the total number of numerical values comprising the classifier
     * parameters.
     * 
     * @return The number of parameters.
     */
    int numParameters();

    /**
     * Obtains a representation of the current
     * classifier parameter values.
     * 
     * @return The parameter values.
     */
    DataVector getParameters();

    /**
     * Updates the classifier parameter values.
     * 
     * @param params - The new parameter values.
     * @return A value of true (or false) if the
     * classifier parameters actually
     * have (or have not) been updated.
     */
    boolean setParameters(DataVector params);

    /**
     * Indicates whether or not it is safe to call the {@link #getGradient}()
     * method.
     * 
     * @return A value of true (or false) if the
     * gradient can (or cannot) be computed.
     */
    boolean hasGradient();

    /**
     * Computes the gradient of the datum score
     * with respect to the classifier parameters,
     * where the gradient of the datum score with respect
     * to the class posteriors is given.
     * 
     * @param datumScore - Specifies the score
     * of the current feature datum.
     * @return The datum score gradient.
     * @throws IllegalStateException If the gradient
     * cannot be computed.
     */
    DataVector getGradient(DatumScore datumScore);

}
