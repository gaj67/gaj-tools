package gaj.analysis.model;

import gaj.analysis.numeric.vector.DataVector;

/**
 * Specifies a model controlled by parameters.
 * <p/>
 * Note: Internally, these parameters need not take the form of a single vector,
 * but must be representable as a vector for external use.
 */
public interface ParameterisedModel extends Model {

    /**
     * Obtains the total number of numerical values comprising the model
     * parameters.
     * 
     * @return The number of parameters.
     */
    int numParameters();

    /**
     * Obtains a representation of the current model parameter values.
     * 
     * @return The parameter values.
     */
    DataVector getParameters();

}
