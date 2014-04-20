package gaj.analysis.vector;

import gaj.data.vector.DataVector;
import gaj.data.vector.DenseVector;
import gaj.data.vector.WritableVector;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements a data vector as a true array of numbers.
 */
/*package-private*/ class WritableDenseVector implements DenseVector, WritableVector {

	protected final double[] values;

	/*package-private*/ WritableDenseVector(double[] values) {
		this.values = values;
	}

	/*package-private*/ WritableDenseVector(int length) {
		this.values = new double[length];
	}

	/*package-private*/ WritableDenseVector(DenseVector vector) {
		this.values = ((WritableDenseVector) vector).values;
	}

	@Override
	public int length() {
		return values.length;
	}

	@Override
	public double get(int pos) {
		return values[pos];
	}

	@Override
	public Iterator<Double> iterator() {
		return new DataIterator() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < values.length;
			}

			@Override
			public Double next() {
				if (hasNext()) return values[index++];
				throw new NoSuchElementException("End of iteration");
			}
		};
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
	public void add(DataVector vector) {
		int i = 0;
		for (double value : vector)
			values[i++] += value;
	}

}
