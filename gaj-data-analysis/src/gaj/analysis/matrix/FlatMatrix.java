package gaj.analysis.matrix;

import gaj.data.vector.DataVector;

/**
 * Indicates that the matrix data is stored as a flat array.
 */
/*package-private*/ abstract class FlatMatrix<T extends DataVector> extends AbstractMatrix<T> {

	protected FlatMatrix(int numRows, int numColumns) {
		super(numRows, numColumns);
	}

	/**
	 * Obtains the internal array of matrix data.
	 * 
	 * @return The flat data array.
	 */
	/*package-private*/ abstract double[] getFlatData();

}
