package gaj.analysis.data.numeric.matrix;

import gaj.analysis.data.numeric.vector.DataVector;

/**
 * Indicates a data matrix with mutable data.
 * Use sparingly, because most methods assume
 * immutable data. In particular, beware
 * of changing data after the matrix has been cached.
 */
public interface MultiplicableMatrix extends DataMatrix {

    /**
     * Multiplies the matrix element by the given value.
     *
     * @param row - The index of the matrix row.
     * @param col - The index of the matrix column.
     * @param value - The value of specified matrix element.
     */
    void multiply(int row, int column, double value);

    /**
     * Multiplies the matrix by the given value.
     *
     * @param value - The scaling value.
     */
    void multiply(double value);

    /**
     * Multiplies a row of the matrix by the given vector.
     *
     * @param row - The index of the matrix row.
     * @param vector - A row vector.
     */
    void multiplyRow(int row, DataVector vector);

    /**
     * Multiplies a row of the matrix by the given value.
     *
     * @param row - The index of the matrix row.
     * @param value - The multiplicative scaling.
     */
    void multiplyRow(int row, double value);

    /**
     * Multiplies a column of the matrix by the given vector.
     *
     * @param column - The index of the matrix column.
     * @param vector - A column vector.
     */
    void multiplyColumn(int column, DataVector vector);

    /**
     * Multiplies a column of the matrix by the given value.
     *
     * @param column - The index of the matrix column.
     * @param value - The multiplicative scaling.
     */
    void multiplyColumn(int column, double value);

    /**
     * Post-multiplies the current matrix by the current matrix.
     * <p/>
     * Note: This is not guaranteed to be efficient.
     *
     * @param matrix - The matrix to add.
     */
    void multiply(DataMatrix matrix);

}
