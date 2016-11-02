package gaj.analysis.data.matrix.impl;

import java.util.Iterator;
import gaj.analysis.data.vector.DataVector;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Iterates over a sequence of data vectors.
 */
public abstract class VectorIterator<T extends DataVector> implements Iterator<T>, Iterable<T> {

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    @Override
    public void remove() {
        throw new NotImplementedException();
    }

}
