package gaj.afl.data.finalsiren;

/**
 * Indicates a parsing error.
 */
public class IllegalParseException extends IllegalStateException {

	private static final long serialVersionUID = -1L;

	protected IllegalParseException(String message) {
		super(message);
	}

	protected IllegalParseException(Throwable e) {
		super(e.getMessage(), e);
	}

	protected IllegalParseException(String message, Throwable e) {
		super(message, e);
	}

}
