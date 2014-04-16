package gaj.data.numeric;

import java.util.Iterator;

/**
 * Provides access to numerical data as an array.
 */
public interface DataMatrix extends Iterable<DataVector> {

	/**
	 * @return The number of rows in the matrix.
	 */
	int numRows();

	/**
	 * @return The number of columns in the matrix.
	 */
	int numColumns();

	/**
	 * Obtains a row of the matrix.
	 * 
	 * @param row - The index of the row in the matrix (counting from 0). 
	 * @return The desired row.
	 */
	DataVector getRow(int row);

	/**
	 * Obtains a column of the matrix.
	 * 
	 * @param col - The index of the column in the matrix (counting from 0). 
	 * @return The desired column.
	 */
	DataVector getColumn(int col);

	/**
	 * Obtains an element of the matrix.
	 * 
	 * @param row - The index of the matrix row.
	 * @param col - The index of the matrix column.
	 * @return The value of specified matrix element.
	 */
	double get(int row, int col);

	/**
	 * Allows iteration over all rows of the data matrix.
	 * 
	 * @return A row iterator.
	 */
	Iterator<DataVector> iterator();

}
