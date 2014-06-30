package gaj.impl.vector;

import gaj.data.vector.DenseVector;

/**
 * Implements the (deferred) scaling of a dense vector by a multiplicative factor.
 */
/*package-private*/ class ScaledDenseVector extends ScaledVector implements DenseVector {

	/*package-private*/ ScaledDenseVector(DenseVector vector, double multiplier) {
		super(vector, multiplier);
	}

}
