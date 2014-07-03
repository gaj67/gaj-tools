package gaj.impl.vector;

import gaj.data.vector.DataVector;

/**
 * Wraps a vector together with a deferred multiplier.
 */
/*package-private*/ class ScaledVector extends AbstractVector {

	private final double multiplier;
	private final DataVector vector;

	/*package-private*/ ScaledVector(DataVector vector, double multiplier) {
		super(vector.size());
		this.vector = vector;
		this.multiplier = multiplier;
	}

	@Override
	public double get(int pos) {
		double value = vector.get(pos);
		return (value == 0) ? 0 : value * multiplier;
	}

	@Override
	protected double _norm() {
		return Math.abs(multiplier) * vector.norm();
	}

	@Override
	public double sum() {
		return multiplier * vector.sum();
	}

	@Override
	public double dot(DataVector vector) {
		return multiplier * this.vector.dot(vector);
	}

	@Override
	public boolean isDense() {
		return vector.isDense();
	}

	@Override
	public boolean isSparse() {
		return vector.isSparse();
	}

	@Override
	public boolean isCompound() {
		return vector.isCompound();
	}

}
