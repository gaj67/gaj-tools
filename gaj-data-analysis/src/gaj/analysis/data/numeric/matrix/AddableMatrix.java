package gaj.analysis.data.numeric.matrix;

import gaj.analysis.data.numeric.vector.DataVector;

/**
 * Indicates a data matrix with mutable data.
 * Use sparingly, because most methods assume
 * immutable data. In particular, beware
 * of changing data after the matrix has been cached.
 */
public interface AddableMatrix extends DataMatrix {

    /**
     * Adds the given value to the value of every matrix element.
     *
     * @param value - The value to be added.
     */
    void add(double value);

    /**
     * Adds the given value to the value of a matrix element.
     *
     * @param row - The index of the matrix row.
     * @param col - The index of the matrix column.
     * @param value - The value of specified matrix element.
     */
    void add(int row, int column, double value);

    /**
     * Adds the given vector to a row of the matrix.
     *
     * @param row - The index of the matrix row.
     * @param vector - A row vector.
     */
    void addRow(int row, DataVector vector);

    /**
     * Adds the given vector to a column of the matrix.
     *
     * @param column - The index of the matrix column.
     * @param vector - A column vector.
     */
    void addColumn(int column, DataVector vector);

    /**
     * Adds the given matrix to the current matrix.
     * <p/>
     * Note: This is not guaranteed to be efficient.
     *
     * @param matrix - The matrix to add.
     */
    void add(DataMatrix matrix);

}
