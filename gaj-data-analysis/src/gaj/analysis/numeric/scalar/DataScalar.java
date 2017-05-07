package gaj.analysis.numeric.scalar;

import gaj.analysis.numeric.DataObject;
import gaj.analysis.numeric.RepresentationType;
import gaj.analysis.numeric.StructureType;

/**
 * Provides access to numerical data as a scalar.
 */
public interface DataScalar extends DataObject {

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
