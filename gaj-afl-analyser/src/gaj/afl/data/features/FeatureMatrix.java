package gaj.afl.data.features;

import java.util.Iterator;

/**
 * Provides access to features that encode
 * information as an array of numbers.
 */
public interface FeatureMatrix extends Iterable<FeatureVector> {

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
	FeatureVector getRow(int row);

	/**
	 * @param col - The index of the column in the matrix (counting from 0). 
	 * @return The desired column.
	 */
	FeatureVector getColumn(int col);

	/**
	 * @param row - The index of the matrix row.
	 * @param col - The index of the matrix column.
	 * @return The value of specified matrix element.
	 */
	double get(int row, int col);

	/**
	 * Pre-multiplies the NxM matrix by the given length-N vector.
	 * 
	 * @param vector - The vector multiplier. 
	 * @return The resulting length-M vector.
	 */
	FeatureVector dotColumns(FeatureVector vector);
	
	/**
	 * Post-multiplies the NxM matrix by the given length-M vector.
	 * 
	 * @param vector - The vector multiplier. 
	 * @return The resulting length-N vector.
	 */
	FeatureVector dotRows(FeatureVector vector);
	
	/**
	 * Allows iteration over all rows of the feature matrix.
	 * 
	 * @return A row iterator.
	 */
	Iterator<FeatureVector> iterator();

}
