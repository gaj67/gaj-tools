package gaj.analysis.numeric.matrix;

import gaj.analysis.numeric.vector.DataVector;

/**
 * Indicates a data matrix with mutable data.
 * Use sparingly, because most methods assume
 * immutable data. In particular, beware
 * of changing data after the matrix has been cached.
 */
public interface SubtractableMatrix extends DataMatrix {

    /**
     * Subtracts the given value from the value of every matrix element.
     *
     * @param value
     *            - The value to be subtracted.
     */
    void subtract(double value);

    /**
     * Subtracts the given value from the value of a matrix element.
     *
     * @param row
     *            - The index of the matrix row.
     * @param col
     *            - The index of the matrix column.
     * @param value
     *            - The value of specified matrix element.
     */
    void subtract(int row, int column, double value);

    /**
     * Subtract the given vector from a row of the matrix.
     *
     * @param row
     *            - The index of the matrix row.
     * @param vector
     *            - A row vector.
     */
    void subtractRow(int row, DataVector vector);

    /**
     * Subtracts the given vector from a column of the matrix.
     *
     * @param column
     *            - The index of the matrix column.
     * @param vector
     *            - A column vector.
     */
    void subtractColumn(int column, DataVector vector);

    /**
     * Subtracts the given matrix from the current matrix.
     * <p/>
     * Note: This is not guaranteed to be efficient.
     *
     * @param matrix
     *            - The matrix to subtract.
     */
    void subtract(DataMatrix matrix);

}
