package gaj.analysis.model;

import gaj.analysis.data.DataObject;

/**
 * Encapsulates the input to a data processor inside the output from the data processor.
 * 
 * <I> - The input data type.
 */
public interface DataInputOutput<I extends DataObject> extends DataObject {

    /**
     * Obtains the data that were input into the data processor.
     * 
     * @return The input object.
     */
    I getData();

}
