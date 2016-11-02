package gaj.analysis.data.vector.impl;

import gaj.analysis.data.object.RepresentationType;

public abstract class SparseVector extends AbstractVector {

    protected SparseVector(int length) {
        super(length);
    }

    @Override
    public RepresentationType representationType() {
        return RepresentationType.SPARSE;
    }

}
