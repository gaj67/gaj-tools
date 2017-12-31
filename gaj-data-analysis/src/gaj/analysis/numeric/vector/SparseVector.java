package gaj.analysis.numeric.vector;

import gaj.analysis.numeric.RepresentationType;

/**
 * Marks a vector as having a sparse representation.
 */
public interface SparseVector extends DataVector {

    @Override
    default RepresentationType representationType() {
        return RepresentationType.SPARSE;
    }

}
