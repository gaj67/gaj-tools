package gaj.data.matrix;


/**
 * Indicates that the matrix data are stored as a nested array
 * of row arrays.
 */
public interface RowMatrix extends DenseMatrix {
	
	double[][] getArray();

}
