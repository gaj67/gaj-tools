package gaj.afl.data.finalsiren;

import java.io.IOException;

/**
 * Indicates an underlying IO exception.
 */
public class UncheckedIOException extends IllegalStateException {

	private static final long serialVersionUID = -1L;

	protected UncheckedIOException(String message, Throwable e) {
		super(message, e);
	}

	protected UncheckedIOException(IOException e) {
		super(e.getMessage(), e);
	}

}
