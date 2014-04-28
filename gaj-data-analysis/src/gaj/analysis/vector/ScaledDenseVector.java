package gaj.analysis.vector;

import gaj.data.vector.DataVector;
import gaj.data.vector.DenseVector;
import gaj.data.vector.WritableVector;

import java.util.Iterator;

/*package-private*/ class ScaledDenseVector extends AbstractVector implements DenseVector {

	private final double multiplier;
	private DenseVector vector;

	/*package-private*/ ScaledDenseVector(DenseVector vector, double multiplier) {
		super(vector.length());
		this.vector = vector;
		this.multiplier = multiplier;
	}

	@Override
	public double get(int pos) {
		return vector.get(pos) * multiplier;
	}

	@Override
	public Iterator<Double> iterator() {
		final Iterator<Double> iter = super.iterator();
		return new DataIterator<Double>() {
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
		int i = 0;
		for (double value : this.vector)
			vector.add(i++, multiplier * value);
	}

}
