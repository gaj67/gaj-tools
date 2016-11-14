package gaj.analysis.model;

/**
 * A deliberately vague marker for output from a data processor.
 */
public interface DataOutput {

    /**
     * Obtains the input data used to compute this output.
     * 
     * @return The input data.
     */
    DataInput getInput();

}
