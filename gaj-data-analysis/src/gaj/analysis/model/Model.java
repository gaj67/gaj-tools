package gaj.analysis.model;

/**
 * A processor of data.
 * 
 * <I> - The input data type.
 * <O> - The output data type.
 */
public interface Model<I, O> {

    /**
     * Processes the input data to compute the output data.
     * 
     * @param input
     *            - The input data.
     * @param info
     *            - Optional auxiliary information.
     * @return The output data.
     */
    O process(I input, AuxiliaryInfo... info);

}
