/*
 * (c) Geoff Jarrad, 2016.
 */
package gaj.iterators.impl;

import gaj.iterators.core.StreamableIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public interface BaseIterator<T> extends StreamableIterator<T> {

    @Override
    default void remove() {
        throw new UnsupportedOperationException("Enforcing stateless iteration");
    }

    default T halt(String message) {
        throw new NoSuchElementException(message);
    }

}