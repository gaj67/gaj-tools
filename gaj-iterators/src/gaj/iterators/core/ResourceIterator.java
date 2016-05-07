/*
 * (c) Geoff Jarrad, 2016.
 */
package gaj.iterators.core;

import java.io.Closeable;
import java.io.UncheckedIOException;

public interface ResourceIterator<T> extends StreamableIterator<T>, Closeable {

	/**
	 *  Idempotently closes the underlying resource.
	 *  @throws UncheckedIOException if the resource cannot be closed.
	 */
	@Override
	public void close();
	
}
