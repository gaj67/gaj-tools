package gaj.analysis.numeric.matrix.impl;

import gaj.analysis.numeric.RepresentationType;
import gaj.analysis.numeric.matrix.DataMatrix;
import gaj.analysis.numeric.matrix.WritableMatrix;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.impl.VectorFactory;
import gaj.common.annotations.PackagePrivate;

/**
 * Implements the scaling of an arbitrary data matrix.
 */
@PackagePrivate class ScaledMatrix extends AbstractMatrix<DataVector> implements DataMatrix {

    private final DataMatrix matrix;
    private final double multiplier;

    @PackagePrivate ScaledMatrix(DataMatrix matrix, double multiplier) {
        super(matrix.numRows(), matrix.numColumns());
        this.matrix = matrix;
        this.multiplier = multiplier;
    }

    @Override
    public double get(int row, int column) {
        return multiplier * matrix.get(row, column);
    }

    @Override
    public DataVector getRow(int row) {
        return VectorFactory.scale(matrix.getRow(row), multiplier);
    }

    @Override
    public DataVector getColumn(int column) {
        return VectorFactory.scale(matrix.getColumn(column), multiplier);
    }

    @Override
    public double norm() {
        return Math.abs(multiplier) * matrix.norm();
    }

    @Override
    public void addTo(WritableMatrix matrix) {
        for (int row = 0; row < numRows; row++)
            matrix.addRow(row, getRow(row));
    }

    @Override
    public RepresentationType representationType() {
        return matrix.representationType();
    }

}
