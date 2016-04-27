/*
 * (c) Geoff Jarrad, 2016.
 */
package gaj.iterators.core;

import java.util.stream.Stream;

public interface Iterative<T> extends Iterable<T>, Streamable<T> {

	@Override
	public StreamableIterator<T> iterator();

	default Stream<T> stream() {
		return iterator().stream();
	}

}
