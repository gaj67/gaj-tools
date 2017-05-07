package gaj.analysis.numeric.matrix.impl;

import gaj.analysis.numeric.RepresentationType;
import gaj.analysis.numeric.matrix.DataMatrix;
import gaj.analysis.numeric.vector.impl.AbstractVector;

/**
 * Presents a matrix as a flat vector of concatenated rows.
 */
public class RowMatrixVector extends AbstractVector {

    protected final DataMatrix matrix;
    protected final int numColumns;

    protected RowMatrixVector(DataMatrix matrix) {
        super(matrix.numRows() * matrix.numColumns());
        this.matrix = matrix;
        this.numColumns = matrix.numColumns();
    }

    @Override
    public double get(int pos) {
        // Row-wise, pos = numColumns * row + column.
        return matrix.get(pos / numColumns, pos % numColumns);
    }

    @Override
    final public RepresentationType representationType() {
        return RepresentationType.COMPOUND;
    }

}
