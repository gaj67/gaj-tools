package gaj.analysis.numeric.vector.impl;

import gaj.analysis.numeric.RepresentationType;

public abstract class DenseVector extends AbstractVector {

    protected DenseVector(int length) {
        super(length);
    }

    @Override
    public RepresentationType representationType() {
        return RepresentationType.DENSE;
    }

}
