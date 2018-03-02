package gaj.analysis.data.numeric;

/**
 * Indicates the underlying type of representation used to store the numerical data elements.
 */
public enum RepresentationType {

    /**
     * Indicates that the data structure has an explicit representation of all elements.
     */
    DENSE,

    /**
     * Indicates that the data structure has an implicit representation of some or all of its elements.
     * Typically, this means that only non-zero elements are explicitly represented.
     * Alternatively, every element might have the same value, so only that value might be kept.
     */
    SPARSE,

    /**
     * Indicates that the data structure has an unknown representation of its elements.
     * Typically, this means that one or more underlying data structures have been
     * wrapped without creating a separate, explicit data structure to store the overall result.
     */
    COMPOUND;

}
