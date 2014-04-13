package gaj.data.numeric;

import java.util.Iterator;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Implements the (deferred) scaling of a data vector by a multiplicative factor.
 * Suited to scaling a compound vector.
 */
/*package-private*/ class ScaledDataVector implements CompoundDataVector {

	private final DataVector vector;
	private final double multiplier;

	/*package-private*/ ScaledDataVector(DataVector vector, double multiplier) {
		this.vector = vector;
		this.multiplier = multiplier;
	}

	@Override
	public int length() {
		return vector.length();
	}

	@Override
	public double get(int pos) {
		return vector.get(pos) * multiplier;
	}

	@Override
	public Iterator<Double> iterator() {
		return new Iterator<Double>() {
			final Iterator<Double> iter = vector.iterator();

			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public Double next() {
				return iter.next() * multiplier;
			}

			@Override
			public void remove() {
				throw new NotImplementedException();
			}
		};
	}

}