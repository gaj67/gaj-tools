package gaj.impl.matrix;

import gaj.data.vector.DataVector;

import java.util.Iterator;

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
