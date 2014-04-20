package gaj.data.matrix;

import java.util.Iterator;

import gaj.data.vector.DataVector;
import gaj.data.vector.WritableVector;

/**
 * Indicates a data matrix with mutable data.
 * Use sparingly, because most methods assume
 * immutable data. In particular, beware
 * of changing data after the matrix has been cached.
 */
public interface WritableMatrix extends DataMatrix {

	/**
	 * Sets the value of a matrix element.
	 * 
	 * @param row - The index of the matrix row.
	 * @param column - The index of the matrix column.
	 * @param value - The value of specified matrix element.
	 */
	void set(int row, int column, double value);

	/**
	 * Adds the given value to the value of a matrix element.
	 * 
	 * @param row - The index of the matrix row.
	 * @param col - The index of the matrix column.
	 * @param value - The value of specified matrix element.
	 */
	void add(int row, int column, double value);

	/**
	 * Adds the given vector to a row of the matrix.
	 * 
	 * @param row - The index of the matrix row.
	 * @param vector - A row vector.
	 */
	void addRow(int row, DataVector vector);

	/**
	 * Adds the given vector to a column of the matrix.
	 * 
	 * @param column - The index of the matrix column.
	 * @param vector - A column vector.
	 */
	void addColumn(int column, DataVector vector);

	/**
	 * Adds the given matrix to the current matrix.
	 * 
	 * @param matrix - The matrix to add.
	 */
	void add(DataMatrix matrix);

	/**
	 * Obtains a row of the matrix.
	 * 
	 * @param row - The index of the row in the matrix (counting from 0). 
	 * @return The desired row.
	 */
	@Override
	WritableVector getRow(int row);

	/**
	 * Allows iteration over all rows of the data matrix.
	 * 
	 * @return A row iterator.
	 */
	@Override
	Iterator<? extends WritableVector> getRows();

	/**
	 * Obtains a column of the matrix.
	 * 
	 * @param column - The index of the column in the matrix (counting from 0). 
	 * @return The desired column.
	 */
	@Override
	WritableVector getColumn(int column);

	/**
	 * Allows iteration over all columns of the data matrix.
	 * 
	 * @return A column iterator.
	 */
	@Override
	Iterator<? extends WritableVector> getColumns();

}
