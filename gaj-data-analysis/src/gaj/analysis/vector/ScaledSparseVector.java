package gaj.analysis.vector;

import java.util.Iterator;

import gaj.data.vector.DataVector;
import gaj.data.vector.SparseVector;
import gaj.data.vector.WritableVector;

/*package-private*/ class ScaledSparseVector extends SparseVectorImpl {

	private final double multiplier;

	/*package-private*/ ScaledSparseVector(SparseVector vector, double multiplier) {
		super(vector);
		this.multiplier = multiplier;
	}

	@Override
	public double get(int pos) {
		return super.get(pos) * multiplier;
	}

	@Override
	public Iterator<Double> iterator() {
		final Iterator<Double> iter = super.iterator();
		return new DataIterator() {
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
		return Math.abs(multiplier) * super.norm();
	}

	@Override
	public double dot(DataVector vector) {
		return multiplier * super.dot(vector);
	}

	@Override
	public void addTo(WritableVector vector) {
		for (int i = 0; i < values.length; i++)
			vector.add(indices[i], multiplier * values[i]);
	}

}
