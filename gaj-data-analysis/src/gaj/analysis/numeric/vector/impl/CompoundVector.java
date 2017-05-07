package gaj.analysis.numeric.vector.impl;

import gaj.analysis.numeric.RepresentationType;

public abstract class CompoundVector extends AbstractVector {

    protected CompoundVector(int length) {
        super(length);
    }

    @Override
    public RepresentationType representationType() {
        return RepresentationType.COMPOUND;
    }

}
