package gaj.analysis.numeric;

import gaj.data.numeric.DataVector;
import gaj.data.numeric.DenseVector;

import java.util.Iterator;
import java.util.NoSuchElementException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Implements a data vector as a true array of numbers.
 */
/*package-private*/ class DenseDataVectorImpl implements DenseVector {

	/*package-private*/ final double[] values;

	/*package-private*/ DenseDataVectorImpl(double[] values) {
		this.values = values;
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
		return new Iterator<Double>() {
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

			@Override
			public void remove() {
				throw new NotImplementedException();
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
	public double[] getValues() {
		return values;
	}

	@Override
	public double norm() {
		double sum = 0;
		for (double value : values)
			sum += value * value;
		return Math.sqrt(sum);
	}

	@Override
	public DataVector scale(double multiplier) {
		double[] newValues = new double[values.length];
		for (int i = 0; i < values.length; i++)
			newValues[i] = multiplier * values[i];
		return new DenseDataVectorImpl(newValues);
	}

}
