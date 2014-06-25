package gaj.impl.vector;

import gaj.data.vector.CompoundVector;

/**
 * Implements the (deferred) scaling of a compound vector by a multiplicative factor.
 */
/*package-private*/ class ScaledCompoundVector extends ScaledVector implements CompoundVector {

	/*package-private*/ ScaledCompoundVector(CompoundVector vector, double multiplier) {
		super(vector, multiplier);
	}

}
