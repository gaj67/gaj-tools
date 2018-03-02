package gaj.analysis.data.numeric.vector;

import gaj.analysis.data.numeric.DataNumeric;
import gaj.analysis.data.numeric.StructureType;

/**
 * Provides access to numerical data as a vector.
 */
public interface DataVector extends DataNumeric {

    @Override
    default StructureType structureType() {
        return StructureType.VECTOR;
    }

    @Override
    default int numDimensions() {
        return 1;
    }

    // ***************************************
    // Intrinsic properties of the vector.

    /**
     * Calculates the Euclidean norm of the vector.
     * 
     * @return The vector norm.
     */
    double norm();

    /**
     * Calculates the sum of the vector elements.
     * 
     * @return The vector sum.
     */
    double sum();

    /**
     * Obtains the value of a vector element.
     * 
     * @param pos - The position of the element (counting from 0).
     * @return The element value.
     */
    double get(int pos);

    // ***************************************
    // Extrinsic properties of the vector.
    // Usually called by factory methods.

    /**
     * Calculates the dot-product of the vector with
     * another vector.
     * <p/>
     * Note: This is not guaranteed to give the most efficient result - use a factory method.
     * 
     * @param vector - The second vector.
     * @return The dot product.
     */
    double dot(DataVector vector);

}
