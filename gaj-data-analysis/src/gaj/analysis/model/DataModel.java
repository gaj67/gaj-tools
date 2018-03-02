package gaj.analysis.model;

import gaj.analysis.data.DataObject;

/**
 * A data model is a data processor that internally encapsulates detailed
 * knowledge about the interpretation and structure of the data.
 * 
 * <I> - The input data type.
 * <O> - The output data type.
 */
public interface DataModel<I extends DataObject, O extends DataObject> extends Model, DataProcessor<I, O> {

}
