package gaj.analysis.model;

import org.eclipse.jdt.annotation.Nullable;

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
     *            - Optional object either specifying auxiliary information for
     *            the processor, or requesting auxiliary information be
     *            provided.
     * @return The output data.
     */
    DataObject process(DataObject input, @Nullable AuxiliaryInfo info);

}
