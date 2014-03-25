/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.properties;

/**
 * An exception thrown if any aspect of the content of a configuration is invalid.
 */
@SuppressWarnings("serial")
public class InvalidPropertiesException extends RuntimeException {

	/*package-only*/ InvalidPropertiesException(String message) {
		super(message);
	}
}