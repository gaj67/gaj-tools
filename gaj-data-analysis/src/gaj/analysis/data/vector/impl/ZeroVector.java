package gaj.analysis.data.vector.impl;

import gaj.analysis.data.vector.DataVector;
import gaj.analysis.data.vector.WritableVector;

/*package-private*/class ZeroVector extends SparseVector {

    /* package-private */ZeroVector(int length) {
        super(length);
    }

    @Override
    public double get(int pos) {
        if (pos < 0 && pos >= length)
            throw new IndexOutOfBoundsException("Bad index: " + pos);
        return 0;
    }

    @Override
    public double dot(DataVector vector) {
        return 0;
    }

    @Override
    protected double _norm() {
        return 0;
    }

    @Override
    public void addTo(WritableVector vector) {
    }

    @Override
    public double sum() {
        return 0;
    }

}
