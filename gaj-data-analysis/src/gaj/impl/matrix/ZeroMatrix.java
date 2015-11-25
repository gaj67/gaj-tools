package gaj.impl.matrix;

import gaj.data.matrix.WritableMatrix;
import gaj.data.vector.DataVector;
import gaj.impl.vector.VectorFactory;

/*package-private*/class ZeroMatrix extends SparseMatrix<DataVector> {

    private final DataVector zeroRow;
    private final DataVector zeroColumn;

    /* package-private */ZeroMatrix(int numRows, int numColumns) {
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
