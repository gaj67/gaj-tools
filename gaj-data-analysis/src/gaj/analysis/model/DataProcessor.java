package gaj.analysis.model;

/**
 * A processor of data.
 */
public interface DataProcessor {

    /**
     * Processes the input data to compute the output data.
     * 
     * @param input
     *            - The input data.
     * @param info
     *            - An object either specifying auxiliary information for the
     *            processor, or requesting auxiliary information be provided.
     * @return The output data.
     */
    DataObject process(DataObject input, AuxiliaryInfo info);

}
