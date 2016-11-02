package gaj.analysis.data.matrix;

import gaj.analysis.data.vector.DataVector;

/**
 * Indicates a data matrix with mutable data.
 * Use sparingly, because most methods assume
 * immutable data. In particular, beware
 * of changing data after the matrix has been cached.
 */
public interface SettableMatrix extends DataMatrix {

    /**
     * Sets the value of a matrix element.
     * 
     * @param row - The index of the matrix row.
     * @param column - The index of the matrix column.
     * @param value - The value of specified matrix element.
     */
    void set(int row, int column, double value);

    /**
     * Copies the given vector to a row of the matrix.
     * 
     * @param row - The index of the matrix row.
     * @param vector - A row vector.
     */
    void setRow(int row, DataVector vector);

    /**
     * Copies the given vector to a column of the matrix.
     * 
     * @param column - The index of the matrix column.
     * @param vector - A column vector.
     */
    void setColumn(int column, DataVector vector);

    /**
     * Copies the given matrix values into the
     * current matrix.
     * 
     * @param matrix - The matrix to copy.
     */
    void set(DataMatrix params);

}
