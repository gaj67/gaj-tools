package gaj.impl.vector;

import gaj.data.object.RepresentationType;

public abstract class DenseVector extends AbstractVector {

    protected DenseVector(int length) {
        super(length);
    }

    @Override
    public RepresentationType representationType() {
        return RepresentationType.DENSE;
    }

}
