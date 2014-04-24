package gaj.analysis.vector;

import gaj.data.vector.CompoundVector;
import gaj.data.vector.DataVector;
import gaj.data.vector.SparseVector;
import gaj.data.vector.WritableVector;

import java.util.Iterator;

/**
 * Provides a view onto part of another vector.
 */
/*package-private*/ class SubVector implements CompoundVector {

	private final DataVector vector;
	protected final int start;
	private final int length;
	private final int end;

	/*package-private*/ public SubVector(DataVector vector, int start, int length) {
		this.vector = vector;
		this.start = start;
		this.length = length;
		this.end = start + length;
	}

	@Override
	public int length() {
		return length;
	}

	@Override
	public double norm() {
		double sum = 0;
		for (int i = start; i < end; i++) {
			double value = vector.get(i);
			sum += value * value;
		}
		return Math.sqrt(sum);
	}

	@Override
	public double get(int pos) {
		return vector.get(start + pos);
	}

	@Override
	public Iterator<Double> iterator() {
		return new VectorIterative<Double>(length) {
			@Override
			protected Double get(int pos) {
				return vector.get(start + pos);
			}
		};
	}

	@Override
	public double dot(DataVector vector) {
		if (vector instanceof SparseVector)
			return vector.dot(this);
		double sum = 0;
		int pos = start;
		for (double value : vector)
			sum += value * this.vector.get(pos++);
		return sum;
	}

	@Override
	public void addTo(WritableVector vector) {
		int i = 0;
		for (int pos = start; pos < end; pos++)
			vector.add(i++, this.vector.get(pos));
	}

}