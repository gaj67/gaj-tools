package gaj.iterators.core;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface StreamableIterator<T> extends Iterator<T>, Streamable<T> {

	default Stream<T> stream() {
		return StreamSupport.stream(
		    Spliterators.spliteratorUnknownSize(this, Spliterator.ORDERED),
		    false
		);
	}

}
