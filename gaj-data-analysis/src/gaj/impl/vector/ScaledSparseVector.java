package gaj.impl.vector;

import gaj.data.vector.SparseVector;

/**
 * Implements the (deferred) scaling of a sparse vector by a multiplicative factor.
 */
/*package-private*/ class ScaledSparseVector extends ScaledVector implements SparseVector {

	/*package-private*/ ScaledSparseVector(SparseVector vector, double multiplier) {
		super(vector, multiplier);
	}

}
