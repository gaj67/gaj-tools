package gaj.analysis.numeric.matrix.impl;

import gaj.analysis.numeric.RepresentationType;
import gaj.analysis.numeric.vector.DataVector;

public abstract class CompoundMatrix<T extends DataVector> extends AbstractMatrix<T> {

    protected CompoundMatrix(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    @Override
    public RepresentationType representationType() {
        return RepresentationType.COMPOUND;
    }

}
