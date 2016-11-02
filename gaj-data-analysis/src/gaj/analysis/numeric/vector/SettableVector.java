package gaj.analysis.numeric.vector;

/**
 * Indicates a data vector with mutable data.
 * Use sparingly, because most methods assume
 * immutable data vectors. In particular, beware
 * of changing data after the vector has been cached.
 */
public interface SettableVector extends DataVector {

    /**
     * Sets the value of a vector element.
     * 
     * @param pos - The index position of the element
     * (counting from 0).
     * @param value - The new element value.
     */
    void set(int pos, double value);

    /**
     * Copies the given vector values into the
     * current vector.
     * 
     * @param vector - A data vector.
     */
    void set(DataVector vector);

}
