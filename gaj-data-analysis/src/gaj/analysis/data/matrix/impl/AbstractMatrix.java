package gaj.analysis.data.matrix.impl;

import gaj.analysis.data.matrix.DataMatrix;
import gaj.analysis.data.matrix.WritableMatrix;
import gaj.analysis.data.object.StructureType;
import gaj.analysis.data.vector.DataVector;

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
    final public int numDimensions() {
        return 2;
    }

    @Override
    final public StructureType structureType() {
        return StructureType.MATRIX;
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
    protected void addTo(WritableMatrix matrix) {
        matrix.add(this);
    }

}
