/*
 * (c) Geoff Jarrad, 2016.
 */
package gaj.iterators.impl;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Specifies a resource backed by IO operations.
 */
public interface IOResource<T> {

    /**
     * Opens the resource for iteration. Will be called implicitly.
     * 
     * @return A non-null sequence over the resource.
     *
     * @throws IOException If the underlying resource fails to be opened.
     */
	T openResource() throws IOException;

    /**
     * Closes the resource. Will be called implicitly.
     *
     * @throws IOException If the underlying resource fails to be closed.
     */
    void closeResource() throws IOException;

    /**
     * Wraps a checked exception, typically an IOException, as an unchecked IO exception.
     *
     * @param e - The underlying exception.
     * @return UncheckedIOException The wrapped exception.
     */
    default UncheckedIOException failure(Exception e) {
		if (e instanceof UncheckedIOException) return (UncheckedIOException) e;
		if (e instanceof IOException) return new UncheckedIOException((IOException) e);
		return new UncheckedIOException(new IOException(e));
    }

    /**
     * Creates an unchecked IO exception with a message.
     *
     * @param message - A description of the cause of the exception.
     * @return UncheckedIOException The exception.
     */
    default UncheckedIOException failure(String message) {
        return new UncheckedIOException(message, new IOException(message));
    }

}
