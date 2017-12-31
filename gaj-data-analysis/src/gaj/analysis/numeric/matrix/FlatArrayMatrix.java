package gaj.analysis.numeric.matrix;

/**
 * Indicates that the matrix data are stored as a single, flat array, and hence the matrix is dense.
 */
public interface FlatArrayMatrix extends DenseMatrix {

    /**
     * Obtains the internal array of matrix data.
     * 
     * @return The flat data array.
     */
    double[] getArray();

}
