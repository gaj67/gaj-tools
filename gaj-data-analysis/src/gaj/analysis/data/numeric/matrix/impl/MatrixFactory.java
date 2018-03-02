package gaj.analysis.data.numeric.matrix.impl;

import java.util.Arrays;
import gaj.analysis.data.numeric.matrix.DataMatrix;
import gaj.analysis.data.numeric.matrix.FlatArrayMatrix;
import gaj.analysis.data.numeric.matrix.RowArrayMatrix;
import gaj.analysis.data.numeric.matrix.WritableMatrix;
import gaj.analysis.data.numeric.vector.DataVector;
import gaj.analysis.data.numeric.vector.WritableVector;
import gaj.analysis.data.numeric.vector.impl.AbstractVector;
import gaj.analysis.data.numeric.vector.impl.VectorFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Provides access to numerical matrices.
 */
public class MatrixFactory {

    private MatrixFactory() {
    }

    /**
     * Creates an immutable matrix of the required size, full of zeroes.
     *
     * @param numRows - The column length.
     * @param numColumns - The row length.
     * @return The zero-data matrix.
     */
    public static DataMatrix newZeroMatrix(int numRows, int numColumns) {
        return new ZeroMatrix(numRows, numColumns);
    }

    /**
     * Wraps the given dense data into a modifiable matrix.
     *
     * @param data - The row-matrix of data.
     * @return The data matrix.
     */
    public static WritableMatrix newMatrix(double[][] data) {
        return new WritableRowMatrix(data);
    }

    /**
     * Creates a writable matrix copy of the given matrix.
     *
     * @param matrix - The matrix to be copied.
     * @return The writable matrix copy.
     */
    public static WritableMatrix newMatrix(DataMatrix matrix) {
        final int numRows = matrix.numRows();
        final int numColumns = matrix.numColumns();
        double[][] copyData = new double[numRows][];
        if (matrix instanceof RowArrayMatrix) {
            int row = 0;
            for (double[] rowData : ((RowArrayMatrix) matrix).getArray()) {
                copyData[row++] = Arrays.copyOf(rowData, numColumns);
            }
        } else {
            int row = 0;
            for (DataVector rowVec : matrix.getRows()) {
                copyData[row++] = VectorFactory.toArray(rowVec);
            }
        }
        return new WritableRowMatrix(copyData);
    }

    /**
     * Creates an initially zero-valued, modifiable matrix.
     *
     * @param numRows - The column length.
     * @param numColumns - The row length.
     * @return The data matrix.
     */
    public static WritableMatrix newMatrix(int numRows, int numColumns) {
        return new WritableRowMatrix(numRows, numColumns);
    }

    public static DataMatrix scale(DataMatrix matrix, double multiplier) {
        if (multiplier == 1) {
            return matrix;
        }
        if (matrix instanceof ZeroMatrix) {
            return matrix;
        }
        if (multiplier == 0) {
            return new ZeroMatrix(matrix.numRows(), matrix.numColumns());
        }
        return new ScaledMatrix(matrix, multiplier);
    }

    /**
     * Sums multiple data matrices.
     *
     * @param matrices - An array of matrices.
     * @return The summed matrix.
     */
    @SuppressWarnings("unchecked")
    public static WritableMatrix add(DataMatrix... matrices) {
        WritableMatrix summedMatrix = newMatrix(matrices[0].numRows(), matrices[0].numColumns());
        for (DataMatrix matrix : matrices) {
            ((AbstractMatrix<DataVector>) matrix).addTo(summedMatrix);
        }
        return summedMatrix;
    }

    /**
     * Post-multiplies the matrix by the given vector.
     *
     * @param matrix - The N x M matrix, A.
     * @param vector - The length-M vector, x.
     * @return A length-N vector, y = A*x.
     */
    public static WritableVector multiply(DataMatrix matrix, DataVector vector) {
        if (matrix instanceof RowArrayMatrix) {
            return multiply((RowArrayMatrix) matrix, vector);
        }
        throw new NotImplementedException();
    }

    private static WritableVector multiply(RowArrayMatrix matrix, DataVector vector) {
        final int numRows = matrix.numRows();
        WritableVector result = VectorFactory.newVector(numRows);
        for (int row = 0; row < numRows; row++) {
            result.set(row, VectorFactory.dot(matrix.getRow(row), vector));
        }
        return result;
    }

    /**
     * Pre-multiplies the matrix by the given vector.
     *
     * @param vector - The length-N vector, x.
     * @param matrix - The N x M matrix, A.
     * @return A length-M vector, y = x*A.
     */
    public static WritableVector multiply(DataVector vector, DataMatrix matrix) {
        if (matrix instanceof RowArrayMatrix) {
            return multiply(vector, (RowArrayMatrix) matrix);
        }
        throw new NotImplementedException();
    }

    private static WritableVector multiply(DataVector vector, RowArrayMatrix matrix) {
        // TODO Better handle a sparse vector.
        WritableVector result = VectorFactory.newVector(matrix.numColumns());
        final int numRows = matrix.numRows();
        for (int row = 0; row < numRows; row++) {
            double value = vector.get(row);
            if (value == 0) {
                continue;
            }
            DataVector scaledVec = VectorFactory.scale(matrix.getRow(row), value);
            ((AbstractVector) scaledVec).addTo(result);
        }
        return result;
    }

    /**
     * Computes the scalar product of two matrices.
     *
     * @param matrix1
     * @param matrix2
     * @return The scalar product.
     */
    public static double dot(DataMatrix matrix1, DataMatrix matrix2) {
        double sum = 0;
        int row = 0;
        for (DataVector vec1 : matrix1.getRows()) {
            DataVector vec2 = matrix2.getRow(row++);
            sum += VectorFactory.dot(vec1, vec2);
        }
        return sum;
    }

    // TODO Similar methods for ColumnMatrix.

    public static void display(String prefix, DataMatrix matrix, String suffix) {
        System.out.printf("%s [%n", prefix);
        for (DataVector row : matrix.getRows()) {
            VectorFactory.display("", row, "\n");
        }
        System.out.printf("]%s", suffix);
    }

    /**
     * Presents the writable matrix as a flat, writable vector.
     *
     * @param matrix
     *            - The N x M matrix.
     * @return A size-NM vector that traverses the matrix row-wise.
     */
    public static WritableVector asVector(WritableMatrix matrix) {
        if (matrix instanceof FlatArrayMatrix) {
            return VectorFactory.newVector(((FlatArrayMatrix) matrix).getArray());
        }
        return new WritableMatrixVector(matrix);
    }

    /**
     * Presents the matrix as a flat vector. If the matrix is writable, then the
     * vector will also be writable.
     *
     * @param matrix
     *            - The N x M matrix.
     * @return A size-NM vector that traverses the matrix row-wise.
     */
    public static DataVector asVector(DataMatrix matrix) {
        if (matrix instanceof WritableMatrix) {
            return asVector((WritableMatrix) matrix);
        }
        if (matrix instanceof FlatArrayMatrix) {
            return VectorFactory.newVector(((FlatArrayMatrix) matrix).getArray());
        }
        return new RowMatrixVector(matrix);
    }

    /**
     * Copies vector data into the matrix, assuming that the vector has the same
     * format (i.e. row-wise traversal) as provided by the asVector(matrix)
     * methods.
     * 
     * @param matrix
     *            - The N x M matrix to be modified.
     * @param vec
     *            - The size-NM vector of new matrix values.
     */
    public static void setFromVector(WritableMatrix matrix, DataVector vec) {
        final int numValues = vec.size();
        if (numValues != matrix.size()) {
            throw new IllegalArgumentException("Expected vector of size " + matrix.size());
        }
        final int numCols = matrix.numColumns();
        int row = 0;
        int col = 0;
        for (int i = 0; i < numValues; i++) {
            matrix.set(row, col, vec.get(i));
            if (++col >= numCols) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Normalises the matrix so that each row-sum is unity.
     *
     * @param matrix - The matrix to be normalised.
     */
    public static void normaliseRowSums(WritableMatrix matrix) {
        for (WritableVector row : matrix.getRows()) {
            row.multiply(1.0 / row.sum());
        }
    }

    /**
     * Determines whether or not two matrices have equal values to the
     * given order of accuracy.
     *
     * @param m1 - The first matrix.
     * @param m2 - The second matrix.
     * @param accuracy - The largest allowable difference.
     * @return A value of true (or false) if the two matrices do (or do not) agree
     * on dimensions and values.
     */
    public static boolean equals(DataMatrix m1, DataMatrix m2, double accuracy) {
        final int numRows = m1.numRows();
        if (m2.numRows() != numRows) {
            return false;
        }
        if (m2.numColumns() != m1.numColumns()) {
            return false;
        }
        for (int row = 0; row < numRows; row++) {
            if (!VectorFactory.equals(m1.getRow(row), m2.getRow(row), accuracy)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sums together the matrix column vectors.
     *
     * @param matrix - The N x M matrix.
     * @return The length-N column vector total.
     */
    public static WritableVector sumColumns(DataMatrix matrix) {
        final int numStates = matrix.numRows();
        WritableVector vec = VectorFactory.newVector(numStates);
        for (int row = 0; row < numStates; row++) {
            vec.set(row, matrix.getRow(row).sum());
        }
        return vec;
    }

    /**
     * Divides each row of the matrix by the corresponding non-zero element of the divisor vector.
     * <p/>
     * Note: If a divisor is zero, then the corresponding row will be zero-valued.
     *
     * @param matrix - The N x M matrix.
     * @param divisors - The length-N column vector of divisors.
     * @return The N x M scaled matrix.
     */
    public static WritableMatrix divideRows(DataMatrix matrix, DataVector divisors) {
        WritableMatrix newMat = MatrixFactory.newMatrix(matrix);
        final int numRows = matrix.numRows();
        for (int row = 0; row < numRows; row++) {
            double divisor = divisors.get(row);
            if (divisor == 0.0) {
                newMat.setRow(row, VectorFactory.newZeroVector(matrix.numColumns()));
            } else {
                newMat.multiplyRow(row, 1.0 / divisor);
            }
        }
        return newMat;
    }

    /**
     * Subtracts the given vector from each row of the matrix.
     *
     * @param matrix
     *            - The N x M matrix.
     * @param rowVec
     *            - The length-M row vector.
     * @return The computed N x M matrix.
     */
    public static WritableMatrix subtractFromRows(DataMatrix matrix, DataVector rowVec) {
        WritableMatrix newMat = MatrixFactory.newMatrix(matrix);
        final int numRows = matrix.numRows();
        for (int row = 0; row < numRows; row++) {
            newMat.getRow(row).subtract(rowVec);
        }
        return newMat;
    }

}
