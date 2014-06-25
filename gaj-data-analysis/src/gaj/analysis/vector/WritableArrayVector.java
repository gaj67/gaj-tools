package gaj.analysis.vector;

import gaj.data.vector.ArrayVector;
import gaj.data.vector.DataVector;
import gaj.data.vector.WritableVector;

/**
 * Implements a data vector as a true array of numbers.
 */
/*package-private*/ class WritableArrayVector extends AbstractVector implements ArrayVector, WritableVector {

	protected final double[] values;

	/*package-private*/ WritableArrayVector(int length) {
		super(length);
		this.values = new double[length];
	}

	/*package-private*/ WritableArrayVector(double[] values) {
		super(values.length);
		this.values = values;
	}

	/*package-private*/ WritableArrayVector(ArrayVector vector) {
		super(vector.length());
		this.values = vector.getArray();
	}

	@Override
	public double get(int pos) {
		return values[pos];
	}

	@Override
	public double dot(DataVector vector) {
		double sum = 0;
		int i = 0;
		for (double value : vector)
			sum += values[i++] * value;
		return sum;
	}

	@Override
	public double norm() {
		double sum = 0;
		for (double value : values)
			sum += value * value;
		return Math.sqrt(sum);
	}

	@Override
	public void addTo(WritableVector vector) {
		for (int i = 0; i < values.length; i++)
			vector.add(i, values[i]);
	}

	@Override
	public void set(int pos, double value) {
		values[pos] = value;
	}

	@Override
	public void add(int pos, double value) {
		values[pos] += value;
	}

	@Override
	public void set(DataVector vector) {
		if (vector instanceof ArrayVector) {
			double[] data = ((ArrayVector)vector).getArray();
			System.arraycopy(data, 0, values, 0, length);
		} else {
			int i = 0;
			for (double value : vector)
				values[i++] = value;
		}
	}

	@Override
	public void add(DataVector vector) {
		if (vector instanceof ArrayVector) {
			double[] data = ((ArrayVector)vector).getArray();
			for (int i = 0; i < length; i++)
				values[i] += data[i];
		} else {
			int i = 0;
			for (double value : vector)
				values[i++] += value;
		}
	}

	@Override
	public double[] getArray() {
		return values;
	}

	@Override
	public void multiply(int pos, double value) {
		values[pos] *= value;
	}

	@Override
	public void multiply(double value) {
		for (int i = 0; i < length; i++)
			values[i] *= value;
	}

	@Override
	public void multiply(DataVector vector) {
		if (vector instanceof ArrayVector) {
			double[] data = ((ArrayVector)vector).getArray();
			for (int i = 0; i < length; i++)
				values[i] *= data[i];
		} else {
			int i = 0;
			for (double value : vector)
				values[i++] *= value;
		}
	}

}
