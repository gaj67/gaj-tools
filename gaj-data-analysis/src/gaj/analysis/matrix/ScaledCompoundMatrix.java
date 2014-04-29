package gaj.analysis.matrix;

import gaj.data.matrix.CompoundMatrix;

/**
 * Specifies that the scaling of a compound matrix is still a compound matrix.
 */
/*package-private*/ class ScaledCompoundMatrix extends ScaledMatrix implements CompoundMatrix {

	/*package-private*/ ScaledCompoundMatrix(CompoundMatrix matrix, double multiplier) {
		super(matrix, multiplier);
	}

}
