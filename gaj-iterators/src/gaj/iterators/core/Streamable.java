/*
 * (c) Geoff Jarrad, 2016.
 */
package gaj.iterators.core;

import java.util.stream.Stream;

public interface Streamable<T> {

	public Stream<T> stream();
	
}
