package gaj.analysis.data.vector;

/**
 * Indicates a data vector with mutable data.
 * Use sparingly, because most methods assume
 * immutable data vectors. In particular, beware
 * of changing data after the vector has been cached.
 */
public interface MultiplicableVector extends DataVector {

    /**
     * Scales a vector element by the given value.
     * 
     * @param pos - The index position of the element
     * (counting from 0).
     * @param value - The scaling value.
     */
    void multiply(int pos, double value);

    /**
     * Scales the entire vector by the given value.
     * 
     * @param pos - The index position of the element
     * (counting from 0).
     * @param value - The scaling value.
     */
    void multiply(double value);

    /**
     * Multiplies the current vector by the given vector.
     * 
     * @param vector - A data vector.
     */
    void multiply(DataVector vector);

}
