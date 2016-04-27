/*
 * (c) Geoff Jarrad, 2016.
 */
package gaj.iterators.core;

import java.util.stream.Stream;

@FunctionalInterface
public interface StreamOp<T,R> {

	public Stream<R> apply(Stream<? extends T> stream);

}
