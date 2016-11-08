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
     * @return The output data.
     */
    DataOutput process(DataInput input);

}
