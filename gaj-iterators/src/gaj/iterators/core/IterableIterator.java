/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.iterators.core;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Encapsulates both the concepts of Iterable and Iterator into a single IterableIterator.
 * 
 * <br/>Note: The key idea is to create <em>efficient</em> iterators that defer the real work
 * for as long as possible. In other words, the constructor shouldn't do all the work up-front
 * and build a simple collection that is iterated over. Instead, the very act of iteration should step
 * through the construction of elements.
 */
public abstract class IterableIterator<T> implements Iterator<T>, Iterable<T> {

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    @Override
    public abstract boolean hasNext();

    @Override
    public abstract T next();

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Enforcing stateless iteration");
    }

    /**
     * Halts the iteration from within next() with the given message.
     * @param message - The iteration termination message.
     * @return No value.
     */
    protected T halt(String message) {
        throw new NoSuchElementException(message);
    }

}