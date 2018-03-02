package gaj.analysis.data.numeric.vector;

import gaj.analysis.data.numeric.RepresentationType;

/**
 * Marks a vector as having a dense representation.
 */
public interface DenseVector extends DataVector {

    @Override
    default RepresentationType representationType() {
        return RepresentationType.DENSE;
    }

}
