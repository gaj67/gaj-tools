package gaj.analysis.data.matrix.impl;

import gaj.analysis.data.object.RepresentationType;
import gaj.analysis.data.vector.DataVector;

public abstract class SparseMatrix<T extends DataVector> extends AbstractMatrix<T> {

    protected SparseMatrix(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    @Override
    public RepresentationType representationType() {
        return RepresentationType.SPARSE;
    }

}
