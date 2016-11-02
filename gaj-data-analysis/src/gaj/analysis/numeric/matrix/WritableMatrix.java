package gaj.analysis.numeric.matrix;

import gaj.analysis.numeric.vector.WritableVector;

/**
 * Indicates a data matrix with mutable data.
 * Use sparingly, because most methods assume
 * immutable data. In particular, beware
 * of changing data after the matrix has been cached.
 */
public interface WritableMatrix extends SettableMatrix, AddableMatrix, MultiplicableMatrix {

    /**
     * Obtains a row of the matrix.
     * 
     * @param row - The index of the row in the matrix (counting from 0).
     * @return The desired row.
     */
    @Override
    WritableVector getRow(int row);

    /**
     * Allows iteration over all rows of the data matrix.
     * 
     * @return A row iterator.
     */
    @Override
    Iterable<? extends WritableVector> getRows();

    /**
     * Obtains a column of the matrix.
     * 
     * @param column - The index of the column in the matrix (counting from 0).
     * @return The desired column.
     */
    @Override
    WritableVector getColumn(int column);

    /**
     * Allows iteration over all columns of the data matrix.
     * 
     * @return A column iterator.
     */
    @Override
    Iterable<? extends WritableVector> getColumns();

}
