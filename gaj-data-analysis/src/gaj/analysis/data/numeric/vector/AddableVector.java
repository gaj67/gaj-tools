package gaj.analysis.data.numeric.vector;

/**
 * Indicates a data vector with mutable data.
 * Use sparingly, because most methods assume
 * immutable data vectors. In particular, beware
 * of changing data after the vector has been cached.
 */
public interface AddableVector extends DataVector {

    /**
     * Adds the given value to the value of every element in the vector.
     *
     * @param value - The value to be added.
     */
    void add(double value);

    /**
     * Adds the given value to the value of a vector element.
     *
     * @param pos - The index position of the element
     * (counting from 0).
     * @param value - The increment in the element value.
     */
    void add(int pos, double value);

    /**
     * Adds the given vector to the current vector.
     * <p/>
     * Note: This is not guaranteed to give the most efficient result - use the factory method.
     *
     * @param vector - A data vector.
     */
    void add(DataVector vector);

}
