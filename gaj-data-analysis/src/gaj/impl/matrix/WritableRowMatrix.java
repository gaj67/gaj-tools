package gaj.impl.matrix;

import gaj.data.matrix.DataMatrix;
import gaj.data.matrix.DenseMatrix;
import gaj.data.matrix.RowMatrix;
import gaj.data.matrix.WritableMatrix;
import gaj.data.vector.ArrayVector;
import gaj.data.vector.DataVector;
import gaj.data.vector.WritableVector;
import gaj.impl.vector.VectorFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/*package-private*/ class WritableRowMatrix extends AbstractMatrix<WritableVector> implements DenseMatrix, WritableMatrix, RowMatrix {

	private final double[][] data;

	/*package-private*/ WritableRowMatrix(double[][] data) {
		super(data.length, data[0].length);
		this.data = data;
	}

	// XXX Shared data, not writable?
	/*package-private*/ WritableRowMatrix(DenseMatrix matrix) {
		super(matrix.numRows(), matrix.numColumns());
		this.data = ((WritableRowMatrix) matrix).data;
	}

	/*package-private*/ WritableRowMatrix(int numRows, int numColumns) {
		super(numRows, numColumns);
		this.data = new double[numRows][numColumns];
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
		throw new NotImplementedException();
	}

	@Override
	public double get(int row, int column) {
		return data[row][column];
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
	public void multiply(int row, int column, double value) {
		data[row][column] *= value;
	}

	@Override
	public void addRow(int row, DataVector vector) {
		final double[] theRow = data[row];
		if (vector instanceof ArrayVector) {
			final double[] values = ((ArrayVector) vector).getArray();
			for (int column = 0; column < numColumns; column++)
				theRow[column] += values[column];
		} else {
			int column = 0;
			for (double value : vector)
				theRow[column++] += value;
		}
	}

	@Override
	public void addColumn(final int column, DataVector vector) {
		if (vector instanceof ArrayVector) {
			final double[] values = ((ArrayVector) vector).getArray();
			for (int row = 0; row < numRows; row++)
				data[row][column] += values[row];
		} else {
			int row = 0;
			for (double value : vector)
				data[row++][column] += value;
		}
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
	public void setRow(final int row, DataVector vector) {
		final double[] theRow = data[row];
		if (vector instanceof ArrayVector) {
			double[] values = ((ArrayVector)vector).getArray();
			System.arraycopy(values, 0, theRow, 0, numColumns);
		} else {
			int column = 0;
			for (double value : vector)
				theRow[column++] = value;
		}
	}

	@Override
	public void setColumn(int column, DataVector vector) {
		if (vector instanceof ArrayVector) {
			final double[] values = ((ArrayVector) vector).getArray();
			for (int row = 0; row < numRows; row++)
				data[row][column] = values[row];
		} else {
			int row = 0;
			for (double value : vector)
				data[row++][column] = value;
		}
	}

	@Override
	public void multiplyRow(int row, DataVector vector) {
		final double[] theRow = data[row];
		if (vector instanceof ArrayVector) {
			final double[] values = ((ArrayVector) vector).getArray();
			for (int column = 0; column < numColumns; column++)
				theRow[column] *= values[column];
		} else {
			int column = 0;
			for (double value : vector)
				theRow[column++] *= value;
		}
	}

	@Override
	public void multiplyColumn(int column, DataVector vector) {
		if (vector instanceof ArrayVector) {
			final double[] values = ((ArrayVector) vector).getArray();
			for (int row = 0; row < numRows; row++)
				data[row][column] *= values[row];
		} else {
			int row = 0;
			for (double value : vector)
				data[row++][column] *= value;
		}
	}

	@Override
	public void multiply(DataMatrix matrix) {
		for (int row = 0; row < numRows; row++)
			multiplyRow(row, matrix.getRow(row));
	}

	@Override
	public double[][] getArray() {
		return data;
	}

}
