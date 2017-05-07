package gaj.analysis.numeric.vector.impl;

import gaj.analysis.numeric.vector.AddableVector;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.SubtractableVector;

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
    public void addTo(AddableVector vector) {
    }

    @Override
    public void subtractFrom(SubtractableVector vector) {
    }

    @Override
    public double sum() {
        return 0;
    }

}
