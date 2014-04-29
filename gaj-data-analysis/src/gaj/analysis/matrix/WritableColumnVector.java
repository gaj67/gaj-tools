package gaj.analysis.matrix;

import gaj.analysis.vector.AbstractVector;
import gaj.data.vector.DataVector;
import gaj.data.vector.WritableVector;

/**
 * Wraps the specified column of a row-based, dense matrix. 
 */
/*package-private*/ class WritableColumnVector extends AbstractVector implements WritableVector {

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
		int row = 0;
		for (double value : vector)
			data[row++][column] = value;
	}

	@Override
	public void add(DataVector vector) {
		int row = 0;
		for (double value : vector)
			data[row++][column] += value;
	}

}
