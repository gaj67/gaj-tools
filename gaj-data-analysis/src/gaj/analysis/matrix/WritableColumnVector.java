package gaj.analysis.matrix;

import gaj.analysis.vector.VectorIterative;
import gaj.data.vector.DataVector;
import gaj.data.vector.WritableVector;

import java.util.Iterator;

/**
 * Wraps the specified column of a row-based, dense matrix. 
 */
/*package-private*/ class WritableColumnVector implements WritableVector {

	private final double[][] data;
	private final int column;
	private final int numRows;

	/*package-private*/ WritableColumnVector(double[][] data, int column) {
		this.data = data;
		this.column = column;
		this.numRows = data.length;
	}

	@Override
	public int length() {
		return numRows;
	}

	@Override
	public double norm() {
		double sum = 0;
		for (int row = 0; row < numRows; row++) {
			double value = data[row][column];
			sum += value * value;
		}
		return Math.sqrt(sum);
	}

	@Override
	public double get(int row) {
		return data[row][column];
	}

	@Override
	public Iterator<Double> iterator() {
		return new VectorIterative<Double>(numRows) {
			@Override
			protected Double get(int row) {
				return data[row][column];
			}
		};
	}

	@Override
	public double dot(DataVector vector) {
		double sum = 0;
		int row = 0;
		for (double value : vector)
			sum += value * data[row++][column];
		return sum;
	}

	@Override
	public void addTo(WritableVector vector) {
		for (int row = 0; row < numRows; row++)
			vector.add(row, data[row][column]);
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
	public void add(DataVector vector) {
		int row = 0;
		for (double value : vector)
			data[row++][column] += value;
	}

}
