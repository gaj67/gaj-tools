/*
 * (c) Geoff Jarrad, 2016.
 */
package gaj.iterators.impl;

import gaj.iterators.core.ResourceIterator;

import java.io.IOException;
import java.util.Iterator;

public abstract class BaseResourceIterator<T> implements BaseIterator<T>, ResourceIterator<T>, IOResource<Iterator<? extends T>> {

	private boolean hasNext = true;
    private Iterator<? extends T> iterator = null;

    protected BaseResourceIterator() {}

	@Override
	public boolean hasNext() {
		if (hasNext) {
	    	if (iterator == null) {
	    		try {
	    			iterator = openResource();
	    		} catch (IOException e) {
	    			throw failure(e);
	    		}
	    	}
	    	hasNext = iterator.hasNext();
	    	if (!hasNext) close();
		}
		return hasNext;
	}

	@Override
	public T next() {
		return hasNext() ? iterator.next() : halt("End of iteration");
	}

    @Override
    public void close() {
		hasNext = false;
    	if (iterator != null) {
   			iterator = null;
        	try {
        		closeResource();
        	} catch (IOException e) {
        		throw failure(e);
        	}
    	}
    }

    @Override
    public void closeResource() throws IOException {}

}
