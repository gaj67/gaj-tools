package gaj.analysis.numeric.matrix.impl;

import gaj.analysis.numeric.RepresentationType;
import gaj.analysis.numeric.vector.DataVector;

public abstract class DenseMatrixImpl<T extends DataVector> extends AbstractMatrix<T> {

    protected DenseMatrixImpl(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    @Override
    public RepresentationType representationType() {
        return RepresentationType.DENSE;
    }

}
