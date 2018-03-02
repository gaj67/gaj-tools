package gaj.analysis.data.numeric.vector;

import gaj.analysis.data.numeric.RepresentationType;

/**
 * Marks a vector as having a compound (i.e. neither dense nor sparse)
 * representation.
 */
public interface CompoundVector extends DataVector {

    @Override
    default RepresentationType representationType() {
        return RepresentationType.COMPOUND;
    }

}
