package gaj.analysis.data.vector;

import gaj.analysis.data.object.RepresentationType;

/**
 * Marks a vector as having a flat array representation,
 * which means it must also be dense.
 */
public interface ArrayVector extends DataVector {

    /**
     * @return The internal array of data.
     */
    double[] getArray();

    @Override
    default RepresentationType representationType() {
        return RepresentationType.DENSE;
    }

}
