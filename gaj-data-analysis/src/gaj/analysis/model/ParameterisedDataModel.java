package gaj.analysis.model;

import gaj.analysis.data.DataObject;
import gaj.analysis.data.numeric.DataNumeric;

/**
 * A parameterised data model.
 * 
 * <I> - The input data type.
 * <O> - The output data type.
 * <T> - The numerical type of model parameterisation.
 */
public interface ParameterisedDataModel<I extends DataObject, O extends DataObject, T extends DataNumeric> 
        extends DataModel<I, O>, ParameterisedModel<T>
{

    /**
     * Determines whether or not gradient information can and should be
     * generated.
     * 
     * @param info
     *            - Optional objects either specifying auxiliary information for
     *            the processor, or requesting that auxiliary information be
     *            provided.
     * @return A value of true (or false) if gradient information will (or will
     *         not) be computed.
     */
    default boolean isGradientComputed(AuxiliaryInfo... info) {
        if (this instanceof GradientAware) {
            for (AuxiliaryInfo anInfo : info) {
                if (anInfo instanceof GradientAware) return true;
            }
        }
        return false;
    }

}
