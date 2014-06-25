package gaj.impl.matrix;

import gaj.data.matrix.DenseMatrix;

/**
 * Specifies that the scaling of a dense matrix is still a dense matrix.
 */
/*package-private*/ class ScaledDenseMatrix extends ScaledMatrix implements DenseMatrix {

	/*package-private*/ ScaledDenseMatrix(DenseMatrix matrix, double multiplier) {
		super(matrix, multiplier);
	}

}
