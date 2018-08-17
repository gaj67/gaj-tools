package gaj.analysis.model;

import gaj.analysis.numeric.vector.DataVector;

/**
 * A data model is a data processor that internally encapsulates detailed
 * knowledge about the interpretation and structure of the data.
 * 
 * <I> - The input data type.
 * <O> - The output data type.
 */
public interface ParameterisedModel<I, O> extends Model<I, O> {

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

    /**
     * Updates the model parameter values, if this is permitted.
     * 
     * @param params
     *            - The new parameter values.
     * @throws IllegalArgumentException
     *             if the parameters cannot be set.
     */
    void setParameters(DataVector params) throws IllegalArgumentException;

    /**
     * Determines whether or not gradient information can and should be
     * generated.
     * 
     * @param info
     *            - Optional auxiliary information.
     * @return A value of true (or false) if gradient information is (or is not)
     *         requested to be computed.
     */
    default boolean computeGradient(AuxiliaryInfo... info) {
        return this instanceof GradientAware && AuxiliaryInfo.isGradientAware(info);
    }

}
