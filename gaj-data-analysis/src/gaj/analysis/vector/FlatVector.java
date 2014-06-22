package gaj.analysis.vector;

import gaj.data.vector.DenseVector;

/**
 * Marks a vector as having a flat array representation,
 * which means it must also be dense.
 */
/*package-private*/ interface FlatVector extends DenseVector {

	/**
	 * @return The internal array of data.
	 */
	double[] getFlatData();

}
