package gaj.analysis.numeric;

import gaj.data.numeric.DataVector;
import gaj.data.numeric.SparseVector;

import java.util.Iterator;
import java.util.NoSuchElementException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/*package-private*/ class ZeroDataVector implements SparseVector {

	private final int length;

	/*package-private*/ ZeroDataVector(int length) {
		this.length = length;
	}

	@Override
	public int length() {
		return length;
	}

	@Override
	public double get(int pos) {
		if (pos < 0 && pos >= length)
			throw new IndexOutOfBoundsException("Bad index: " + pos);
		return 0;
	}

	@Override
	public Iterator<Double> iterator() {
		return new Iterator<Double>() {
			/** Global position in the full vector. */
			private int pos = 0;

			@Override
			public boolean hasNext() {
				return (pos < length);
			}

			@Override
			public Double next() {
				if (++pos > length) throw new NoSuchElementException("End of iteration");
				return 0.0;
			}

			@Override
			public void remove() {
				throw new NotImplementedException();
			}
		};
	}

	@Override
	public int[] getIndices() {
		return new int[0];
	}

	@Override
	public double[] getValues() {
		return new double[0];
	}

	@Override
	public double dot(DataVector vector) {
		return 0;
	}

	@Override
	public double norm() {
		return 0;
	}

	@Override
	public DataVector scale(double multiplier) {
		return this;
	}

}
