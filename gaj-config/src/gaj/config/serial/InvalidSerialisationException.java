/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serial;

/**
 * This error is thrown when serialisation or deserialisation
 * is commenced but unexpectedly fails.
 */
@SuppressWarnings("serial")
public class InvalidSerialisationException extends RuntimeException
{

	/*package-private*/ InvalidSerialisationException(String message) {
		super(message);
	}

	public InvalidSerialisationException(Throwable t) {
		super(t.getMessage());
	}

}