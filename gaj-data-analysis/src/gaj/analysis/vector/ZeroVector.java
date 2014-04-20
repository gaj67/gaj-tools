package gaj.analysis.vector;

import gaj.data.vector.DataVector;
import gaj.data.vector.SparseVector;
import gaj.data.vector.WritableVector;

import java.util.Iterator;
import java.util.NoSuchElementException;

/*package-private*/ class ZeroVector implements SparseVector {

	private final int length;

	/*package-private*/ ZeroVector(int length) {
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
		return new VectorIterative(length) {
			@Override
			protected double get(int pos) {
				return 0;
			}
		};
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
	public void addTo(WritableVector vector) {}

}
