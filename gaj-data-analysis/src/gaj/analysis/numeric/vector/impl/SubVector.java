package gaj.analysis.numeric.vector.impl;

import gaj.analysis.numeric.vector.AddableVector;
import gaj.analysis.numeric.vector.CompoundVector;
import gaj.analysis.numeric.vector.DataVector;
import gaj.common.annotations.PackagePrivate;

/**
 * Provides a view onto part of another vector.
 */
@PackagePrivate
class SubVector extends AbstractVector implements CompoundVector {

    private final DataVector vector;
    protected final int start;
    protected final int end;

    @PackagePrivate public SubVector(DataVector vector, int start, int length) {
        super(length);
        this.vector = vector;
        this.start = start;
        this.end = start + length;
    }

    @Override
    protected double _norm() {
        double sum = 0;
        for (int pos = start; pos < end; pos++) {
            double value = vector.get(pos);
            sum += value * value;
        }
        return Math.sqrt(sum);
    }

    @Override
    public double sum() {
        double sum = 0;
        for (int pos = start; pos < end; pos++)
            sum += vector.get(pos);
        return sum;
    }

    @Override
    public double get(int pos) {
        return vector.get(start + pos);
    }

    @Override
    public double dot(DataVector vector) {
        double sum = 0;
        for (int i = 0, pos = start; pos < end; pos++, i++)
            sum += vector.get(i) * this.vector.get(pos);
        return sum;
    }

    @Override
    public void addTo(AddableVector vector) {
        for (int i = 0, pos = start; pos < end; pos++, i++)
            vector.add(i, this.vector.get(pos));
    }

}
