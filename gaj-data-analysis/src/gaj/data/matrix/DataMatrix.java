package gaj.data.matrix;

import java.util.Iterator;

import gaj.data.numeric.DataObject;
import gaj.data.vector.DataVector;


/**
 * Provides access to numerical data as an array.
 */
public interface DataMatrix extends DataObject {

	/**
	 * @return The number of rows in the matrix.
	 */
	int numRows();

	/**
	 * @return The number of columns in the matrix.
	 */
	int numColumns();

	/**
	 * Obtains an element of the matrix.
	 * 
	 * @param row - The index of the matrix row.
	 * @param column - The index of the matrix column.
	 * @return The value of specified matrix element.
	 */
	double get(int row, int column);

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
	 * @param column - The index of the column in the matrix (counting from 0). 
	 * @return The desired column.
	 */
	DataVector getColumn(int column);

	/**
	 * Allows iteration over all rows of the data matrix.
	 * 
	 * @return A row iterator.
	 */
	Iterator<? extends DataVector> getRows();

	/**
	 * Allows iteration over all columns of the data matrix.
	 * 
	 * @return A column iterator.
	 */
	Iterator<? extends DataVector> getColumns();

	/**
	 * Obtains the norm of the matrix.
	 * 
	 * @return The matrix norm.
	 */
	double norm();

	/**
	 * Adds the current matrix values to the given matrix.
	 * 
	 * @param matrix - A modifiable matrix.
	 */
	void addTo(WritableMatrix matrix);

}
