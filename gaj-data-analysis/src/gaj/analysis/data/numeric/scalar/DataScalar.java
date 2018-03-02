package gaj.analysis.data.numeric.scalar;

import gaj.analysis.data.numeric.DataNumeric;
import gaj.analysis.data.numeric.RepresentationType;
import gaj.analysis.data.numeric.StructureType;

/**
 * Provides access to numerical data as a scalar.
 */
public interface DataScalar extends DataNumeric {

    @Override
    default StructureType structureType() {
        return StructureType.SCALAR;
    }

    @Override
    default RepresentationType representationType() {
        return RepresentationType.DENSE;
    }

    @Override
    default int numDimensions() {
        return 0;
    }

    @Override
    default int size() {
        return 1;
    }

    /**
     * Obtains the scalar value.
     * 
     * @return The value.
     */
    double get();

}
