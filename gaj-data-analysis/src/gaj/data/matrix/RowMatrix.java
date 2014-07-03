package gaj.data.matrix;


/**
 * Indicates that the matrix data are stored as a nested array
 * of row arrays, and hence the matrix is dense.
 */
public interface RowMatrix extends DataMatrix {
	
	double[][] getArray();

}
