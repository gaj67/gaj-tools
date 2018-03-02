package gaj.analysis.data.numeric.matrix.impl;

import gaj.analysis.data.numeric.RepresentationType;
import gaj.analysis.data.numeric.vector.DataVector;

public abstract class SparseMatrix<T extends DataVector> extends AbstractMatrix<T> {

    protected SparseMatrix(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    @Override
    public RepresentationType representationType() {
        return RepresentationType.SPARSE;
    }

}
