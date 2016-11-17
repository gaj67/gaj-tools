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
     * @param includeAuxiliary
     *            - A flag indicating whether (true) or not (false) to include
     *            auxiliary information (e.g. gradient, Hessian, etc.) in the
     *            output.
     * @return The output data.
     */
    DataOutput process(DataInput input, boolean includeAuxiliary);

}
