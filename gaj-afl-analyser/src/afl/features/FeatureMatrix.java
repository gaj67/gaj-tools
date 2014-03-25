package afl.features;

public interface FeatureMatrix {

	/**
	 * @return The number of rows in the matrix.
	 */
	int numRows();
	/**
	 * @return The number of columns in the matrix.
	 */
	int numColumns();
	/**
	 * @param row - The index of the row in the matrix (counting from 0). 
	 * @return The desired row.
	 */
	FeatureVector get(int row);
	/**
	 * @param row - The index of the matrix row.
	 * @param col - The index of the matrix column.
	 * @return The value of specified matrix element.
	 */
	double get(int row, int col);
	/**
	 * @param vector - The column vector to pre-multiply by the matrix. 
	 * @return The dot product of the matrix with the given vector.
	 */
	double[] dot(FeatureVector vector);
	
}
