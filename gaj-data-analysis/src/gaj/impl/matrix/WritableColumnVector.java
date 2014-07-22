package gaj.impl.matrix;

import gaj.data.vector.ArrayVector;
import gaj.data.vector.DataVector;
import gaj.data.vector.WritableVector;
import gaj.impl.vector.DenseVector;

/**
 * Wraps the specified column of a row-based, dense matrix. 
 */
/*package-private*/ class WritableColumnVector extends DenseVector implements WritableVector {

	private final double[][] data;
	private final int column;

	/*package-private*/ WritableColumnVector(double[][] data, int column) {
		super(data.length);
		this.data = data;
		this.column = column;
	}

	@Override
	public double get(int row) {
		return data[row][column];
	}

	@Override
	public void set(int row, double value) {
		data[row][column] = value;
	}

	@Override
	public void add(int row, double value) {
		data[row][column] += value;
	}

	@Override
	public void set(DataVector vector) {
		if (vector instanceof ArrayVector) {
			final double[] values = ((ArrayVector) vector).getArray();
			for (int row = 0; row < length; row++)
				data[row][column] = values[row];
		} else {
			int row = 0;
			for (double value : vector)
				data[row++][column] = value;
		}
	}

	@Override
	public void add(DataVector vector) {
		if (vector instanceof ArrayVector) {
			final double[] values = ((ArrayVector) vector).getArray();
			for (int row = 0; row < length; row++)
				data[row][column] += values[row];
		} else {
			int row = 0;
			for (double value : vector)
				data[row++][column] += value;
		}
	}

	@Override
	public void multiply(int row, double value) {
		data[row][column] *= value;
	}

	@Override
	public void multiply(double value) {
		for (int row = 0; row < length; row++)
			data[row][column] *= value;
	}

	@Override
	public void multiply(DataVector vector) {
		if (vector instanceof ArrayVector) {
			final double[] values = ((ArrayVector) vector).getArray();
			for (int row = 0; row < length; row++)
				data[row][column] *= values[row];
		} else {
			int row = 0;
			for (double value : vector)
				data[row++][column] *= value;
		}
	}

}
