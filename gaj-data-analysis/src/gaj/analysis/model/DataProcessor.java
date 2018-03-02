package gaj.analysis.model;

import gaj.analysis.data.DataObject;

/**
 * A processor of data.
 * 
 * <I> - The input data type.
 * <O> - The output data type.
 */
public interface DataProcessor<I extends DataObject, O extends DataObject> {

    /**
     * Processes the input data to compute the output data.
     * 
     * @param input
     *            - The input data.
     * @param info
     *            - Optional objects either specifying auxiliary information for
     *            the processor, or requesting that auxiliary information be
     *            provided.
     * @return The output data.
     */
    O process(I input, AuxiliaryInfo... info);

}
