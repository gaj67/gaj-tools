package gaj.impl.matrix;

import gaj.data.vector.DataVector;

public abstract class SparseMatrix<T extends DataVector> extends AbstractMatrix<T> {

    protected SparseMatrix(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    @Override
    public boolean isDense() {
        return false;
    }

    @Override
    public boolean isSparse() {
        return true;
    }

    @Override
    public boolean isCompound() {
        return false;
    }

}
