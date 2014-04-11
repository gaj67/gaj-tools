package gaj.data.numeric;

import java.util.Iterator;
import java.util.NoSuchElementException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Implements a data vector as a true array of numbers.
 */
/*package-private*/ class DenseDataVector implements DataVector {

	/*package-private*/ final double[] data;

	/*package-private*/ DenseDataVector(double[] data) {
		this.data = data;
	}

	@Override
	public int length() {
		return data.length;
	}

	@Override
	public double get(int pos) {
		return data[pos];
	}

	@Override
	public Iterator<Double> iterator() {
		return new Iterator<Double>() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < data.length;
			}

			@Override
			public Double next() {
				if (hasNext()) return data[index++];
				throw new NoSuchElementException("End of iteration");
			}

			@Override
			public void remove() {
				throw new NotImplementedException();
			}
		};
	}

}
