package gaj.iterators.impl;

import gaj.iterators.core.ResourceIterative;
import gaj.iterators.core.ResourceIterator;

public abstract class ResourceSubIterative<T> implements ResourceIterative<T> {

	private ResourceIterator<T> iterator = null;

	protected ResourceSubIterative() {}

	@Override
	public ResourceIterator<T> iterator() {
		closeIterator();
		iterator = newIterator();
		return iterator;
	}

	protected abstract ResourceIterator<T> newIterator();

	@Override
	public void close() {
		closeIterator();
		iterator = null;
	}

	private void closeIterator() {
		if (iterator != null) {
			iterator.close();
		}		
	}

}
