package gaj.analysis.data.numeric;

/**
 * Indicates the type of structure used to contain numerical data elements.
 */
public enum StructureType {

    /**
     * Indicates that the data structure has a (zero-dimensional) single element.
     */
    SCALAR,

    /**
     * Indicates that the data structure has a one-dimensional vector of elements.
     */
    VECTOR,

    /**
     * Indicates that the data structure has a two-dimensional matrix of elements.
     */
    MATRIX,

    /**
     * Indicates that the data structure has an n-dimensional structure of elements, with n >= 3.
     */
    TENSOR;

}
