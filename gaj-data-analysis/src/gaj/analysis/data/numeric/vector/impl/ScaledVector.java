package gaj.analysis.data.numeric.vector.impl;

import gaj.analysis.data.numeric.vector.CompoundVector;
import gaj.analysis.data.numeric.vector.DataVector;
import gaj.common.annotations.PackagePrivate;

/**
 * Wraps a vector together with a deferred multiplier.
 */
@PackagePrivate class ScaledVector extends AbstractVector implements CompoundVector {

    private final double multiplier;
    private final DataVector vector;

    @PackagePrivate ScaledVector(DataVector vector, double multiplier) {
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

}
