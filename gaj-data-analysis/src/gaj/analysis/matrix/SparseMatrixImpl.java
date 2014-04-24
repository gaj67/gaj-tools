package gaj.analysis.matrix;

import java.util.Iterator;

import gaj.data.matrix.SparseMatrix;
import gaj.data.matrix.WritableMatrix;
import gaj.data.vector.DataVector;

/**
 * Implements a matrix that is sparse in both dimensions.
 */
/*package-private*/ class SparseMatrixImpl implements SparseMatrix {

	private final int numRows;
	private final int numColumns;
	private final int size;
	private final int[] rows;
	private final int[] columns;
	private final double[] values;

	/*package-private*/ SparseMatrixImpl(int numRows, int numColumns, int[] rows, int[] columns, double[] values) {
		this.numRows = numRows;
		this.numColumns = numColumns;
		size = rows.length;
		this.rows = rows;
		this.columns = columns;
		this.values = values;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataVector getColumn(int column) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double get(int row, int column) {
		for (int i = 0; i < size; i++) {
			if (rows[i] == row && columns[i] == column)
				return values[i];
		}
		return 0;
	}

	@Override
	public double norm() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addTo(WritableMatrix matrix) {
		for (int i = 0; i < size; i++)
			matrix.add(rows[i], columns[i], values[i]);
	}

	@Override
	public Iterator<? extends DataVector> getRows() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<? extends DataVector> getColumns() {
		// TODO Auto-generated method stub
		return null;
	}

}
