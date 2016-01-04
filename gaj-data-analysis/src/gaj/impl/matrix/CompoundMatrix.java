package gaj.impl.matrix;

import gaj.data.object.RepresentationType;
import gaj.data.vector.DataVector;

public abstract class CompoundMatrix<T extends DataVector> extends AbstractMatrix<T> {

    protected CompoundMatrix(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    @Override
    public RepresentationType representationType() {
        return RepresentationType.COMPOUND;
    }

}
