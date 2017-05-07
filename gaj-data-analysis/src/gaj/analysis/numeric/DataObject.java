package gaj.analysis.numeric;

/**
 * Indicates dimensionalised numerical data, e.g. a vector or matrix or tensor, etc., or a
 * collection of such data objects.
 */
public interface DataObject {

    /**
     * Indicates the type of data structure.
     * 
     * @return The structure type.
     */
    StructureType structureType();

    /**
     * Indicates the type of representation of the data structure.
     * 
     * @return The representation type.
     */
    RepresentationType representationType();

    /**
     * Indicates the total number of elements in the data structure.
     * 
     * @return The size of the data.
     */
    int size();

    /**
     * Indicates the number of dimensions of the data structure.
     * 
     * @return The number of dimensions.
     */
    int numDimensions();

    /**
     * Indicates the size of the data structure along each dimension.
     * 
     * @return An array of dimensions.
     */
    // int[] dimensions();

}
