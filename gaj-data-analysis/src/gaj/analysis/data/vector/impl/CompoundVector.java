package gaj.analysis.data.vector.impl;

import gaj.analysis.data.object.RepresentationType;

public abstract class CompoundVector extends AbstractVector {

    protected CompoundVector(int length) {
        super(length);
    }

    @Override
    public RepresentationType representationType() {
        return RepresentationType.COMPOUND;
    }

}
