package gaj.analysis.data.numeric.vector.impl;

import java.util.NoSuchElementException;

/**
 * Provides an iterator over vector elements.
 */
public abstract class VectorIterative<T> extends DataIterator<T> {

    protected final int length;
    protected int pos = 0;

    /**
     * Specifies the length of the vector.
     * 
     * @param length - The vector length.
     */
    protected VectorIterative(int length) {
        this.length = length;
    }

    @Override
    public boolean hasNext() {
        return (pos < length);
    }

    @Override
    public T next() {
        if (!hasNext())
            throw new NoSuchElementException("End of iteration");
        return get(pos++);
    }

    /**
     * Obtains the vector element at the specified position.
     * 
     * @param pos - The index position of the element
     * (starting from 0).
     * @return The element value.
     */
    protected abstract T get(int pos);

}
