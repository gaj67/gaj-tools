package gaj.analysis.matrix;

import gaj.analysis.vector.AbstractVector;
import gaj.analysis.vector.VectorFactory;
import gaj.data.matrix.DataMatrix;
import gaj.data.matrix.DenseMatrix;
import gaj.data.matrix.RowMatrix;
import gaj.data.matrix.WritableMatrix;
import gaj.data.vector.DataVector;
import gaj.data.vector.WritableVector;

/*package-private*/ class WritableDenseMatrix extends AbstractMatrix<WritableVector> implements DenseMatrix, WritableMatrix, RowMatrix {

	private final double[][] data;

	/*package-private*/ WritableDenseMatrix(double[][] data) {
		super(data.length, data[0].length);
		this.data = data;
	}

	// XXX Shared data, not writable?
	/*package-private*/ WritableDenseMatrix(DenseMatrix matrix) {
		super(matrix.numRows(), matrix.numColumns());
		this.data = ((WritableDenseMatrix) matrix).data;
	}

	/*package-private*/ WritableDenseMatrix(int numRows, int numColumns) {
		super(numRows, numColumns);
		this.data = new double[numRows][numColumns];
	}

	@Override
	public double get(int row, int column) {
		return data[row][column];
	}

	@Override
	public WritableVector getRow(int row) {
		return VectorFactory.newWritableVector(data[row]);
	}

	@Override
	public WritableVector getColumn(int column) {
		return new WritableColumnVector(data, column);
	}

	@Override
	public double norm() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void set(int row, int column, double value) {
		data[row][column] = value;
	}

	@Override
	public void add(int row, int column, double value) {
		data[row][column] += value;
	}

	@Override
	public void addRow(int row, DataVector vector) {
		((AbstractVector) vector).addTo(getRow(row));
	}

	@Override
	public void addColumn(int column, DataVector vector) {
		int row = 0;
		for (double value : vector)
			data[row++][column] += value;
	}

	@Override
	public void add(DataMatrix matrix) {
		for (int row = 0; row < numRows; row++)
			addRow(row, matrix.getRow(row));
	}

	@Override
	protected void addTo(WritableMatrix matrix) {
		for (int row = 0; row < numRows; row++)
			matrix.addRow(row, getRow(row));
	}

	@Override
	public void set(DataMatrix matrix) {
		for (int row = 0; row < numRows; row++)
			setRow(row, matrix.getRow(row));
	}

	@Override
	public void setRow(int row, DataVector vector) {
		final double[] theRow = data[row];
		int column = 0;
		for (double value : vector)
			theRow[column++] = value;
	}

	@Override
	public void setColumn(int column, DataVector vector) {
		int row = 0;
		for (double value : vector)
			data[row++][column] = value;
	}

}
