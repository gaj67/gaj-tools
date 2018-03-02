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

}
