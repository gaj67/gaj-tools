package gaj.impl.matrix;

import gaj.data.matrix.SparseMatrix;

/**
 * Specifies that the scaling of a sparse matrix is still a sparse matrix.
 */
/*package-private*/ class ScaledSparseMatrix extends ScaledMatrix implements SparseMatrix {

	/*package-private*/ ScaledSparseMatrix(SparseMatrix matrix, double multiplier) {
		super(matrix, multiplier);
	}

	// TODO Create a sparse addTo() method.
}
