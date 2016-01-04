package gaj.impl.vector;

import gaj.data.object.RepresentationType;

public abstract class CompoundVector extends AbstractVector {

    protected CompoundVector(int length) {
        super(length);
    }

    @Override
    public RepresentationType representationType() {
        return RepresentationType.COMPOUND;
    }

}
