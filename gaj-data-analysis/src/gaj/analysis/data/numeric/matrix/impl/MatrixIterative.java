package gaj.analysis.data.numeric.matrix.impl;

import java.util.NoSuchElementException;
import gaj.analysis.data.numeric.vector.DataVector;

/**
 * Provides an iterator over matrix elements
 * as a sequence of vectors.
 */
public abstract class MatrixIterative<T extends DataVector> extends VectorIterator<T> {

    private final int length;
    private int pos = 0;

    /**
     * Specifies the number of vectors in the matrix.
     * 
     * @param length - The matrix 'length'.
     */
    protected MatrixIterative(int length) {
        this.length = length;
    }

    @Override
    public boolean hasNext() {
        return (pos < length);
    }

    @Override
    public T next() {
        if (pos >= length)
            throw new NoSuchElementException("End of iteration");
        return get(pos++);
    }

    /**
     * Obtains the vector at the specified 'position' in the matrix.
     * 
     * @param pos - The index position of the vector.
     * (starting from 0).
     * @return The vector.
     */
    protected abstract T get(int pos);

}
