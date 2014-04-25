package gaj.analysis.vector;

import gaj.data.vector.DataVector;
import gaj.data.vector.SparseVector;
import gaj.data.vector.WritableVector;

import java.util.Iterator;

/**
 * Implements a data vector as an array of index/value pairs, with ascending indices.
 */
/*package-private*/ class SparseVectorImpl extends AbstractVector implements SparseVector {

	/*package-private*/ final int[] indices;
	/*package-private*/ final double[] values;

	/*package-private*/ SparseVectorImpl(int length, int[] indices, double[] values) {
		super(length);
		this.indices = indices;
		this.values = values;
	}

	/*package-private*/ SparseVectorImpl(int length) {
		super(length);
		this.indices = new int[0];
		this.values = new double[0];
	}

	/*package-private*/ SparseVectorImpl(SparseVector vector) {
		super(vector.length());
		SparseVectorImpl sVec = (SparseVectorImpl) vector;
		this.indices = sVec.indices;
		this.values = sVec.values;
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
		return new VectorIterative<Double>(length) {
			/** Local position in the index table. */
			private int index = 0;

			@Override
			protected Double get(int pos) {
				while (index < indices.length && pos > indices[index]) index++;
				if (index >= indices.length) return 0.0;
				if (pos < indices[index]) return 0.0;
				return values[index++];
			}
		};
	}

	@Override
	public double dot(DataVector vector) {
		double sum = 0;
		for (int i = 0; i < indices.length; i++)
			sum += values[i] * vector.get(indices[i]);
		return sum;
	}

	@Override
	public double norm() {
		double sum = 0;
		for (double value : values)
			sum += value * value;
		return Math.sqrt(sum);
	}

	@Override
	public void addTo(WritableVector vector) {
		for (int i = 0; i < values.length; i++)
			vector.add(indices[i], values[i]);
	}

}
