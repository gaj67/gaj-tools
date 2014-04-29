package gaj.analysis.matrix;

import gaj.data.matrix.RowMatrix;
import gaj.data.matrix.WritableMatrix;
import gaj.data.vector.DataVector;

import java.util.Iterator;
import java.util.NoSuchElementException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/*package-private*/ class RowMatrixImpl implements RowMatrix {

	private final DataVector[] rows;
	private final int numRows;
	private final int numColumns;

	/*package-private*/ RowMatrixImpl(DataVector[] rows) {
		this.rows = rows;
		numRows = rows.length;
		numColumns = rows[0].length();
	}
	
	@Override
	public int numRows() {
		return numRows;
	}

	@Override
	public int numColumns() {
		return numColumns;
	}

	@Override
	public DataVector getRow(int row) {
		return rows[row];
	}

	@Override
	public DataVector getColumn(int col) {
		throw new NotImplementedException();
	}

	@Override
	public double get(int row, int col) {
		return rows[row].get(col);
	}

	@Override
	public Iterator<DataVector> iterator() {
		return new Iterator<DataVector>() {
			private int row = 0;

			@Override
			public boolean hasNext() {
				return row < numRows;
			}

			@Override
			public DataVector next() {
				if (row >= numRows) throw new NoSuchElementException("End of iteration");
				return rows[row++];
			}

			@Override
			public void remove() {
				throw new NotImplementedException();
			}
		};
	}

	@Override
	public double norm() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addTo(WritableMatrix matrix) {
		// TODO Auto-generated method stub
		
	}

}
