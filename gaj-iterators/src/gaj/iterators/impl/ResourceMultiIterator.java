/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.iterators.impl;

import gaj.iterators.core.ResourceIterator;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Encapsulates the notion of iterating through a sequence of iterators.
 * This provides a flattened sequence of items obtain from a sequence of sequences.
 */
public abstract class ResourceMultiIterator<T> extends MultiIterator<T> implements ResourceIterator<T> {

    protected ResourceMultiIterator() {}

    @Override
    public boolean hasNext() {
        if (hasNext) {
            if (iterator == null) {
                iterator = nextIterator();
            }
            while (iterator != null && !iterator.hasNext()) {
            	closeIterator();
                iterator = nextIterator();
            }
            if (iterator == null) {
                hasNext = false;
            }
        }
        return hasNext;
    }

	private void closeIterator() {
		if (iterator instanceof AutoCloseable) {
			try {
				((AutoCloseable) iterator).close();
			} catch (Exception e) {
				throw failure(e);
			}
		} else if (iterator instanceof Closeable) {
			try {
				((Closeable) iterator).close();
			} catch (IOException e) {
				throw failure(e);
			}
		}
	}

	private UncheckedIOException failure(Exception e) {
		if (e instanceof UncheckedIOException) return (UncheckedIOException) e;
		if (e instanceof IOException) return new UncheckedIOException((IOException) e);
		return new UncheckedIOException(new IOException(e));
	}

	@Override
	public void close() {
		closeIterator();
		iterator = null;
	}

}
