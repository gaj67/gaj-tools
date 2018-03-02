package gaj.analysis.data.numeric.vector;

import gaj.analysis.data.numeric.RepresentationType;

/**
 * Marks a vector as having a sparse representation.
 */
public interface SparseVector extends DataVector {

    @Override
    default RepresentationType representationType() {
        return RepresentationType.SPARSE;
    }

}
