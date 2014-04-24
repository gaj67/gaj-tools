package gaj.analysis.vector;

import gaj.data.vector.CompoundVector;
import gaj.data.vector.DataVector;
import gaj.data.vector.WritableVector;

import java.util.Iterator;

/**
 * Implements the (deferred) scaling of a data vector by a multiplicative factor.
 * Suited to scaling a compound vector.
 */
/*package-private*/ class ScaledCompoundVector implements CompoundVector {

	private final DataVector vector;
	private final double multiplier;

	/*package-private*/ ScaledCompoundVector(DataVector vector, double multiplier) {
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
		return new DataIterator<Double>() {
			final Iterator<Double> iter = vector.iterator();

			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public Double next() {
				return iter.next() * multiplier;
			}
		};
	}

	@Override
	public double norm() {
		return Math.abs(multiplier) * vector.norm();
	}

	@Override
	public double dot(DataVector vector) {
		return multiplier * this.vector.dot(vector);
	}

	@Override
	public void addTo(WritableVector vector) {
		int i = 0;
		for (double value : this.vector)
			vector.add(i++, multiplier * value);
	}

}
