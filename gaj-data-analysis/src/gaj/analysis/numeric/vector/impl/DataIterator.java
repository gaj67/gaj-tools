package gaj.analysis.numeric.vector.impl;

import java.util.Iterator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Iterates over data elements.
 */
public abstract class DataIterator<T> implements Iterator<T> {

    @Override
    public void remove() {
        throw new NotImplementedException();
    }

}
