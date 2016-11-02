package gaj.analysis.classifier;

import gaj.analysis.data.vector.DataVector;

/**
 * Specifies a model controlled by parameters.
 * <p/>
 * Note: These parameters need not take the form of a single vector, but must be representable as a vector for external manipulation, e.g. parameter search for score optimisation.
 */
public interface Parameterised {

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

}
