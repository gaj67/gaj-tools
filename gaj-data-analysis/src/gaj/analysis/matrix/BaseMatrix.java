package gaj.analysis.matrix;

import gaj.data.vector.DataVector;

import java.util.Iterator;

/**
 * Implements a simple interface for iterating over
 * rows or columns of a matrix.
 */
/*package-private*/ abstract class BaseMatrix<T extends DataVector> {

	protected final int numRows;
	protected final int numColumns;

	protected BaseMatrix(int numRows, int numColumns) {
		this.numRows = numRows;
		this.numColumns = numColumns;
	}

	public int numRows() {
		return numRows;
	}

	public int numColumns() {
		return numColumns;
	}

	public Iterator<T> getRows() {
		return new MatrixIterative<T>(numRows) {
			@Override
			protected T get(int row) {
				return getRow(row);
			}
		};
	}

	public abstract T getRow(int row);

	public Iterator<T> getColumns() {
		return new MatrixIterative<T>(numColumns) {
			@Override
			protected T get(int column) {
				return getColumn(column);
			}
		};
	}

	public abstract T getColumn(int column);

}
