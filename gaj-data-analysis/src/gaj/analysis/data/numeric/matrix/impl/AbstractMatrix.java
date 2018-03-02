package gaj.analysis.data.numeric.matrix.impl;

import gaj.analysis.data.numeric.matrix.AddableMatrix;
import gaj.analysis.data.numeric.matrix.DataMatrix;
import gaj.analysis.data.numeric.matrix.SubtractableMatrix;
import gaj.analysis.data.numeric.vector.DataVector;

/**
 * Implements a simple interface for iterating over
 * rows or columns of a matrix.
 */
public abstract class AbstractMatrix<T extends DataVector> implements DataMatrix {

    protected final int numRows;
    protected final int numColumns;

    protected AbstractMatrix(int numRows, int numColumns) {
        this.numRows = numRows;
        this.numColumns = numColumns;
    }

    @Override
    final public int numRows() {
        return numRows;
    }

    @Override
    final public int numColumns() {
        return numColumns;
    }

    @Override
    final public int size() {
        return numRows * numColumns;
    }

    @Override
    public Iterable<T> getRows() {
        return new MatrixIterative<T>(numRows) {
            @Override
            protected T get(int row) {
                return getRow(row);
            }
        };
    }

    @Override
    public abstract T getRow(int row);

    @Override
    public Iterable<T> getColumns() {
        return new MatrixIterative<T>(numColumns) {
            @Override
            protected T get(int column) {
                return getColumn(column);
            }
        };
    }

    @Override
    public abstract T getColumn(int column);

    /**
     * Adds the current matrix values to the given matrix.
     * 
     * @param matrix - A modifiable matrix.
     */
    protected void addTo(AddableMatrix matrix) {
        matrix.add(this);
    }

    /**
     * Subtracts the current matrix values from the given matrix.
     * 
     * @param matrix
     *            - A modifiable matrix.
     */
    protected void subtractFrom(SubtractableMatrix matrix) {
        matrix.subtract(this);
    }

}
