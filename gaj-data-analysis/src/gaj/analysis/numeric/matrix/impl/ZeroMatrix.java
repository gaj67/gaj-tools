package gaj.analysis.numeric.matrix.impl;

import gaj.analysis.numeric.matrix.WritableMatrix;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.impl.VectorFactory;
import gaj.common.annotations.PackagePrivate;

@PackagePrivate class ZeroMatrix extends SparseMatrix<DataVector> {

    private final DataVector zeroRow;
    private final DataVector zeroColumn;

    @PackagePrivate ZeroMatrix(int numRows, int numColumns) {
        super(numRows, numColumns);
        zeroRow = VectorFactory.newZeroVector(numColumns);
        zeroColumn = VectorFactory.newZeroVector(numRows);
    }

    @Override
    public double norm() {
        return 0;
    }

    @Override
    public DataVector getRow(int row) {
        return zeroRow;
    }

    @Override
    public DataVector getColumn(int col) {
        return zeroColumn;
    }

    @Override
    public double get(int row, int col) {
        return 0;
    }

    @Override
    protected void addTo(WritableMatrix matrix) {
    }

}
