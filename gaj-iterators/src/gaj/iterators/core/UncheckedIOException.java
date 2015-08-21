/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.iterators.core;

/**
 * Provides a mechanism for allowing the propagation of a runtime-based IOException,
 * in situations where a true IOException is not permitted.
 */
@SuppressWarnings("serial")
public class UncheckedIOException extends RuntimeException {

    /**
     * Creates an unchecked IO exception.
     * 
     * @param message - A description of the cause of the exception.
     * @return The exception.
     */
    /*package-private*/ UncheckedIOException(String message) {
        super(message);
    }

    /**
     * Wraps a checked exception, typically an IOException, as an unchecked IO exception.
     * 
     * @param e - The underlying exception.
     * @return The wrapped exception.
     */
    /*package-private*/ UncheckedIOException(Throwable e) {
        super(e.getMessage(), e);
    }

}