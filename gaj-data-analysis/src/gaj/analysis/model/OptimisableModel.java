package gaj.analysis.model;

import gaj.analysis.numeric.vector.DataVector;

/**
 * Specifies a parameterised model that is optimisable.
 * <p/>
 * Note: Internally, these parameters need not take the form of a single vector,
 * but must be representable as a vector for external manipulation.
 */
public interface OptimisableModel extends ParameterisedModel {

    /**
     * Updates the model parameter values.
     * 
     * @param params
     *            - The new parameter values.
     * @return A value of true (or false) if the model parameters actually have
     *         (or have not) been updated.
     */
    boolean setParameters(DataVector params);

}
