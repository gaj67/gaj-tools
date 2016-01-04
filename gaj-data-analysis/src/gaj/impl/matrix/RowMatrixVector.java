package gaj.impl.matrix;

import gaj.data.matrix.DataMatrix;
import gaj.data.object.RepresentationType;
import gaj.impl.vector.AbstractVector;

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
