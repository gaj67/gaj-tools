package gaj.analysis.numeric.matrix;

import gaj.analysis.numeric.RepresentationType;

/**
 * Indicates that the matrix values are stored in a dense representation.
 */
public interface DenseMatrix extends DataMatrix {

    @Override
    default RepresentationType representationType() {
        return RepresentationType.DENSE;
    }

}
