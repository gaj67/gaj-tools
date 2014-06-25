package gaj.impl.vector;

import gaj.data.vector.DataVector;

/**
 * Wraps a vector together with a deferred multiplier.
 */
/*package-private*/ class ScaledVector extends AbstractVector {

	private final double multiplier;
	private DataVector vector;

	/*package-private*/ ScaledVector(DataVector vector, double multiplier) {
		super(vector.length());
		this.vector = vector;
		this.multiplier = multiplier;
	}

	@Override
	public double get(int pos) {
		return vector.get(pos) * multiplier;
	}

	@Override
	public double norm() {
		return Math.abs(multiplier) * vector.norm();
	}

	@Override
	public double dot(DataVector vector) {
		return multiplier * this.vector.dot(vector);
	}

}
