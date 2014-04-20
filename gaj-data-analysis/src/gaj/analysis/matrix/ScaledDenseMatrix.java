package gaj.analysis.matrix;

import gaj.data.matrix.DataMatrix;
import gaj.data.matrix.DenseMatrix;

/*package-private*/ class ScaledDenseMatrix extends WritableDenseRowMatrix implements DataMatrix {

	private final double multiplier;

	/*package-private*/ ScaledDenseMatrix(DenseMatrix matrix, double multiplier) {
		super(matrix);
		this.multiplier = multiplier;
	}

}
