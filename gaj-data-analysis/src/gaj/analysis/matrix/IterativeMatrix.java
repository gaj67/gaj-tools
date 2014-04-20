package gaj.analysis.matrix;

import java.util.Iterator;
import java.util.NoSuchElementException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import gaj.data.vector.DataVector;

/**
 * Implements a simple interface for iterating over
 * rows and columns of a matrix.
 */
/*package-private*/ abstract class IterativeMatrix<T extends DataVector> {

	protected final int numRows;
	protected final int numColumns;

	protected IterativeMatrix(int numRows, int numColumns) {
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
		return new Iterator<T>() {
			private int row = 0;
			
			@Override
			public boolean hasNext() {
				return (row < numRows);
			}

			@Override
			public T next() {
				if (row >= numRows)
					throw new NoSuchElementException("End of iteration");
				return getRow(row);
			}

			@Override
			public void remove() {
				throw new NotImplementedException();
			}
		};
	}

	public abstract T getRow(int row);

	public Iterator<T> getColumns() {
		return new Iterator<T>() {
			private int column = 0;
			
			@Override
			public boolean hasNext() {
				return (column < numColumns);
			}

			@Override
			public T next() {
				if (column >= numColumns)
					throw new NoSuchElementException("End of iteration");
				return getColumn(column);
			}

			@Override
			public void remove() {
				throw new NotImplementedException();
			}
		};
	}

	public abstract T getColumn(int column);

}
