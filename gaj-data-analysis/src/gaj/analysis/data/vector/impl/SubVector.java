package gaj.analysis.data.vector.impl;

import gaj.analysis.data.vector.AddableVector;
import gaj.analysis.data.vector.DataVector;
import gaj.common.annotations.PackagePrivate;

/**
 * Provides a view onto part of another vector.
 */
@PackagePrivate class SubVector extends CompoundVector {

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
        for (int i = start; i < end; i++) {
            double value = vector.get(i);
            sum += value * value;
        }
        return Math.sqrt(sum);
    }

    @Override
    public double sum() {
        double sum = 0;
        for (int i = start; i < end; i++)
            sum += vector.get(i);
        return sum;
    }

    @Override
    public double get(int pos) {
        return vector.get(start + pos);
    }

    @Override
    public double dot(DataVector vector) {
        double sum = 0;
        int pos = start;
        for (double value : vector)
            sum += value * this.vector.get(pos++);
        return sum;
    }

    @Override
    public void addTo(AddableVector vector) {
        int i = 0;
        for (int pos = start; pos < end; pos++)
            vector.add(i++, this.vector.get(pos));
    }

}
