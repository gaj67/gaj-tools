package gaj.analysis.numeric;

import gaj.data.numeric.CompoundVector;
import gaj.data.numeric.DataVector;

import java.util.Iterator;
import java.util.NoSuchElementException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Implements the (deferred) concatenation of multiple data vectors together into a single, compound vector.
 */
/*package-private*/ class ConcatenatedDataVector implements CompoundVector {

	private final DataVector[] vectors;
	private final int length;

	/*package-private*/ ConcatenatedDataVector(DataVector[] vectors) {
		this.vectors = vectors;
		int _length = 0;
		for (DataVector vector : vectors)
			_length += vector.length();
		this.length = _length;
	}

	@Override
	public int length() {
		return length;
	}

	@Override
	public double get(final int pos) {
		if (pos >= 0 && pos < length) {
			int _pos = pos;
			for (DataVector vector : vectors) {
				if (_pos < vector.length()) return vector.get(_pos);
				_pos -= vector.length();
			}
		}
		throw new IndexOutOfBoundsException("Bad index: " + pos);
	}

	@Override
	public Iterator<Double> iterator() {
		return new Iterator<Double>() {
			int i = 0;
			Iterator<Double> iter = null;

			@Override
			public boolean hasNext() {
				if (i >= vectors.length) return false;
				if (iter == null) iter = vectors[0].iterator();
				while (!iter.hasNext() && i < vectors.length)
					iter = vectors[++i].iterator();
				return (i < vectors.length);
			}

			@Override
			public Double next() {
				if (!hasNext()) throw new NoSuchElementException("End of iteration"); 
				return iter.next();
			}

			@Override
			public void remove() {
				throw new NotImplementedException();
			}
		};
	}

	@Override
	public double norm() {
		double sum = 0;
		for (DataVector vector : vectors) {
			double norm = vector.norm();
			sum += norm * norm;
		}
		return Math.sqrt(sum);
	}

}
