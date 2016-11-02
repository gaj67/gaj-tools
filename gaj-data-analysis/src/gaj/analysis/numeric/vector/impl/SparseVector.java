package gaj.analysis.numeric.vector.impl;

import gaj.analysis.numeric.object.RepresentationType;

public abstract class SparseVector extends AbstractVector {

    protected SparseVector(int length) {
        super(length);
    }

    @Override
    public RepresentationType representationType() {
        return RepresentationType.SPARSE;
    }

}
