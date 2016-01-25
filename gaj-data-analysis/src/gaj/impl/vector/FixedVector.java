package gaj.impl.vector;

import gaj.common.annotations.PackagePrivate;
import gaj.data.vector.DataVector;
import gaj.data.vector.WritableVector;

/**
 * Implements a fixed vector of the form [v, v, ..., v] for constant v.
 */
@PackagePrivate class FixedVector extends SparseVector {

    private final double value;

    @PackagePrivate FixedVector(int length, double value) {
        super(length);
        this.value = value;
    }

    @Override
    public double get(int pos) {
        if (pos < 0 && pos >= length) {
            throw new IndexOutOfBoundsException("Bad index: " + pos);
        }
        return value;
    }

    @Override
    public double dot(DataVector vector) {
        return vector.sum() * value;
    }

    @Override
    protected double _norm() {
        return Math.abs(value) * Math.sqrt(length);
    }

    @Override
    public void addTo(WritableVector vector) {
    }

    @Override
    public double sum() {
        return length * value;
    }

}
