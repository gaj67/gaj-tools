package gaj.impl.matrix;

import gaj.data.matrix.DataMatrix;
import gaj.data.matrix.FlatArrayMatrix;
import gaj.data.matrix.RowArrayMatrix;
import gaj.data.matrix.WritableMatrix;
import gaj.data.vector.DataVector;
import gaj.data.vector.WritableVector;
import gaj.impl.vector.AbstractVector;
import gaj.impl.vector.VectorFactory;
import java.util.Arrays;
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
    public static DataVector multiply(DataMatrix matrix, DataVector vector) {
        if (matrix instanceof RowArrayMatrix) {
            return multiply((RowArrayMatrix) matrix, vector);
        }
        throw new NotImplementedException();
    }

    private static DataVector multiply(RowArrayMatrix matrix, DataVector vector) {
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
    public static DataVector multiply(DataVector vector, DataMatrix matrix) {
        if (matrix instanceof RowArrayMatrix) {
            return multiply(vector, (RowArrayMatrix) matrix);
        }
        throw new NotImplementedException();
    }

    private static DataVector multiply(DataVector vector, RowArrayMatrix matrix) {
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
     * @param matrix - The N x M matrix.
     * @return A length-NM vector.
     */
    public static WritableVector asVector(WritableMatrix matrix) {
        if (matrix instanceof FlatArrayMatrix) {
            return VectorFactory.newVector(((FlatArrayMatrix) matrix).getArray());
        }
        return new WritableVectorMatrix(matrix);
    }

    /**
     * Presents the matrix as a flat vector.
     *
     * @param matrix - The N x M matrix.
     * @return A length-NM vector.
     */
    public static DataVector asVector(DataMatrix matrix) {
        if (matrix instanceof FlatArrayMatrix) {
            return VectorFactory.newVector(((FlatArrayMatrix) matrix).getArray());
        }
        return new VectorMatrix(matrix);
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

}
