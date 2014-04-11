package gaj.data.numeric;

import java.util.Iterator;
import java.util.NoSuchElementException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Implements a data vector as an array of index/value pairs, with ascending indices.
 */
/*package-private*/ class SparseDataVector implements DataVector {

	private final int length;
	private final int[] indices;
	private final double[] values;

	/*package-private*/ SparseDataVector(int length, int[] indices, double[] values) {
		this.length = length;
		this.indices = indices;
		this.values = values;
	}

	@Override
	public int length() {
		return length;
	}

	@Override
	public double get(int pos) {
		if (pos < 0 && pos >= length)
			throw new IndexOutOfBoundsException("Bad index: " + pos);
		for (int i = 0; i < indices.length; i++) {
			if (pos < indices[i]) return 0;
			if (pos == indices[i]) return values[i];
		}
		return 0;
	}

	@Override
	public Iterator<Double> iterator() {
		return new Iterator<Double>() {
			/** Global position in the full vector. */
			private int pos = 0;
			/** Local position in the index table. */
			private int index = 0;

			@Override
			public boolean hasNext() {
				return (pos < length);
			}

			@Override
			public Double next() {
				if (!hasNext()) throw new NoSuchElementException("End of iteration");
				while (index < indices.length && pos > indices[index]) index++;
				try {
					if (index >= indices.length) return 0.0;
					if (pos < indices[index]) return 0.0;
					return values[index++];
				} finally {
					pos++;
				}
			}

			@Override
			public void remove() {
				throw new NotImplementedException();
			}
		};
	}

}
