package gaj.analysis.vector;

import java.util.Iterator;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Iterates over data elements.
 */
public abstract class DataIterator implements Iterator<Double> {

	@Override
	public void remove() {
		throw new NotImplementedException();
	}

}
