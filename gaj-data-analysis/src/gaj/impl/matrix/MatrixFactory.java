package gaj.impl.matrix;

import gaj.data.matrix.CompoundMatrix;
import gaj.data.matrix.DataMatrix;
import gaj.data.matrix.DenseMatrix;
import gaj.data.matrix.FlatMatrix;
import gaj.data.matrix.RowMatrix;
import gaj.data.matrix.SparseMatrix;
import gaj.data.matrix.WritableMatrix;
import gaj.data.vector.DataVector;
import gaj.data.vector.WritableVector;
import gaj.impl.vector.AbstractVector;
import gaj.impl.vector.VectorFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Provides access to numerical matrices.
 */
public class MatrixFactory {

	private MatrixFactory() {}

	/**
	 * Creates a matrix of the required length, full of zeroes.
	 * 
	 * @param numRows - The column length.
	 * @param numColumns - The row length.
	 * @return The zero-data matrix.
	 */
	public static DataMatrix newMatrix(int numRows, int numColumns) {
		return new ZeroMatrix(numRows, numColumns);
	}

	/**
	 * Wraps the given dense data into a matrix.
	 * <p/>Warning: The data must not be modified!
	 * 
	 * @param data - The row-matrix of data.
	 * @return The data matrix.
	 */
	public static RowMatrix newMatrix(double[][] data) {
		return new WritableRowMatrix(data);
	}

	/**
	 * Creates an initially zero-valued, modifiable matrix.
	 * 
	 * @param numRows - The column length.
	 * @param numColumns - The row length.
	 * @return The data matrix.
	 */
	public static WritableMatrix newWritableMatrix(int numRows, int numColumns) {
		return new WritableRowMatrix(numRows, numColumns);
	}

	/**
	 * Wraps the given data into a modifiable matrix.
	 * 
	 * @param data - The row-matrix of data.
	 * @return The data matrix.
	 */
	public static WritableMatrix newWritableMatrix(double[][] data) {
		return new WritableRowMatrix(data);
	}

	public static DataMatrix scale(DataMatrix matrix, double multiplier) {
		if (multiplier == 1) return matrix;
		if (matrix instanceof ZeroMatrix) return matrix;
		if (multiplier == 0) return new ZeroMatrix(matrix.numRows(), matrix.numColumns());
		if (matrix instanceof SparseMatrix)
			return new ScaledSparseMatrix((SparseMatrix) matrix, multiplier);
		if (matrix instanceof DenseMatrix)
			return new ScaledDenseMatrix((DenseMatrix) matrix, multiplier);
		if (matrix instanceof CompoundMatrix)
			return new ScaledCompoundMatrix((CompoundMatrix) matrix, multiplier);
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
		WritableMatrix summedMatrix = newWritableMatrix(matrices[0].numRows(), matrices[0].numColumns());
		for (DataMatrix matrix : matrices)
			((AbstractMatrix<DataVector>) matrix).addTo(summedMatrix);
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
		if (matrix instanceof RowMatrix)
			return multiply((RowMatrix) matrix, vector);
		throw new NotImplementedException();
	}

	private static DataVector multiply(RowMatrix matrix, DataVector vector) {
		final int numRows = matrix.numRows();
		WritableVector result = VectorFactory.newWritableVector(numRows);
		for (int row = 0; row < numRows; row++)
			result.set(row, VectorFactory.dot(matrix.getRow(row), vector));
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
		if (matrix instanceof RowMatrix)
			return multiply(vector, (RowMatrix) matrix);
		throw new NotImplementedException();
	}
	
	private static DataVector multiply(DataVector vector, RowMatrix matrix) {
		// TODO Better handle a sparse vector.
		WritableVector result = VectorFactory.newWritableVector(matrix.numColumns());
		final int numRows = matrix.numRows();
		for (int row = 0; row < numRows; row++) {
			double value = vector.get(row);
			if (value == 0) continue;
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
		for (DataVector row : matrix.getRows())
			VectorFactory.display("", row, "\n");
		System.out.printf("]%s", suffix);
	}

	public static WritableVector asWritableVector(WritableMatrix matrix) {
		if (matrix instanceof FlatMatrix)
			return VectorFactory.newWritableVector(((FlatMatrix) matrix).getArray());
		return new WritableVectorMatrix(matrix);
	}

	public static DataVector asVector(DataMatrix matrix) {
		if (matrix instanceof FlatMatrix)
			return VectorFactory.newVector(((FlatMatrix) matrix).getArray());
		return new VectorMatrix(matrix);
	}

}
