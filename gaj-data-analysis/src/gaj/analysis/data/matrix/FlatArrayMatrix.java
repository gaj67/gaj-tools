package gaj.analysis.data.matrix;

/**
 * Indicates that the matrix data are stored as a single, flat array, and hence the matrix is dense.
 */
public interface FlatArrayMatrix extends DataMatrix {

    /**
     * Obtains the internal array of matrix data.
     * 
     * @return The flat data array.
     */
    double[] getArray();

}
