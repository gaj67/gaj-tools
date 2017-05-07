package gaj.analysis.numeric.vector;

/**
 * Indicates a data vector with mutable data.
 * Use sparingly, because most methods assume
 * immutable data vectors. In particular, beware
 * of changing data after the vector has been cached.
 */
public interface SubtractableVector extends DataVector {

    /**
     * Subtracts the given value from the value of every element in the vector.
     *
     * @param value
     *            - The value to be subtracted.
     */
    void subtract(double value);

    /**
     * Subtracts the given value from the value of a vector element.
     *
     * @param pos
     *            - The index position of the element (counting from 0).
     * @param value
     *            - The decrement in the element value.
     */
    void subtract(int pos, double value);

    /**
     * Subtracts the given vector from the current vector.
     * <p/>
     * Note: This is not guaranteed to give the most efficient result - use the
     * factory method.
     *
     * @param vector
     *            - A data vector.
     */
    void subtract(DataVector vector);

}
