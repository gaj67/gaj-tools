package gaj.impl.vector;

import gaj.data.vector.DataVector;
import gaj.data.vector.WritableVector;

import java.util.Iterator;

/**
 * Implements the (deferred) concatenation of multiple data vectors together into a single, compound vector.
 */
/*package-private*/ class ConcatenatedVector extends CompoundVector {

	private final DataVector[] vectors;

	/*package-private*/ ConcatenatedVector(DataVector[] vectors) {
		super(computeLength(vectors));
		this.vectors = vectors;
	}

	private static int computeLength(DataVector[] vectors) {
		int length = 0;
		for (DataVector vector : vectors)
			length += vector.size();
		return length;
	}

	@Override
	public double get(final int pos) {
		if (pos >= 0 && pos < length) {
			int _pos = pos;
			for (DataVector vector : vectors) {
				if (_pos < vector.size()) return vector.get(_pos);
				_pos -= vector.size();
			}
		}
		throw new IndexOutOfBoundsException("Bad index: " + pos);
	}

	@Override
	public Iterator<Double> iterator() {
		return new VectorIterative<Double>(length) {
			private Iterator<Double> iter = null;
			private int i = 0;

			@Override
			public boolean hasNext() {
				if (i >= vectors.length) return false;
				if (iter == null) iter = vectors[0].iterator();
				while (!iter.hasNext() && ++i < vectors.length)
					iter = vectors[i].iterator();
				return (i < vectors.length);
			}

			@Override
			protected Double get(int pos) {
				return iter.next();
			}
		};
	}

	@Override
	protected double _norm() {
		double sum = 0;
		for (DataVector vector : vectors) {
			double norm = vector.norm();
			sum += norm * norm;
		}
		return Math.sqrt(sum);
	}

	@Override
	public double sum() {
		double sum = 0;
		for (DataVector vector : vectors)
			sum += vector.sum();
		return sum;
	}

	@Override
	public double dot(DataVector vector) {
		double sum = 0;
		int pos = 0;
		for (DataVector vector1 : vectors) {
			DataVector vector2 = new SubVector(vector, pos, vector1.size());
			sum += vector1.dot(vector2);
			pos += vector1.size();
		}
		return sum;
	}

	@Override
	public void addTo(WritableVector vector) {
		int pos = 0;
		for (DataVector vector1 : vectors) {
			WritableVector vector2 = new WritableSubVector(vector, pos, vector1.size());
			((AbstractVector) vector1).addTo(vector2);
			pos += vector1.size();
		}
	}

}
