package gaj.analysis.matrix;

import gaj.data.matrix.DataMatrix;
import gaj.data.matrix.WritableMatrix;
import gaj.data.vector.DataVector;

/**
 * Implements a simple interface for iterating over
 * rows or columns of a matrix.
 */
public abstract class AbstractMatrix<T extends DataVector> implements DataMatrix {

	protected final int numRows;
	protected final int numColumns;

	protected AbstractMatrix(int numRows, int numColumns) {
		this.numRows = numRows;
		this.numColumns = numColumns;
	}

	public int numRows() {
		return numRows;
	}

	public int numColumns() {
		return numColumns;
	}

	public Iterable<T> getRows() {
		return new MatrixIterative<T>(numRows) {
			@Override
			protected T get(int row) {
				return getRow(row);
			}
		};
	}

	public abstract T getRow(int row);

	public Iterable<T> getColumns() {
		return new MatrixIterative<T>(numColumns) {
			@Override
			protected T get(int column) {
				return getColumn(column);
			}
		};
	}

	public abstract T getColumn(int column);

	/**
	 * Adds the current matrix values to the given matrix.
	 * 
	 * @param matrix - A modifiable matrix.
	 */
	protected void addTo(WritableMatrix matrix) {
		matrix.add(this);
	}

}
