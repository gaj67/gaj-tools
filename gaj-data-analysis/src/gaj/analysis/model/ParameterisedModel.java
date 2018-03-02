package gaj.analysis.model;

import gaj.analysis.data.numeric.DataNumeric;

/**
 * Specifies a model controlled by parameters.
 * 
 * <T> - The numerical type of parameterisation.
 */
public interface ParameterisedModel<T extends DataNumeric> extends Model {

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
    T getParameters();

    /**
     * Updates the model parameter values, if this is permitted.
     * 
     * @param params
     *            - The new parameter values.
     * @throws IllegalArgumentException
     *             if the parameters cannot be set.
     */
    void setParameters(T params) throws IllegalArgumentException;

}
