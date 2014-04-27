/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.declaration;

/**
 * An exception thrown if any aspect of the content of a configuration is invalid.
 */
@SuppressWarnings("serial")
public class InvalidDeclarationException extends RuntimeException {

	/*package-private*/ InvalidDeclarationException(String message) {
		super(message);
	}
}