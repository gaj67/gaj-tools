/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.properties;

/**
 * An exception thrown if any aspect of the content of a configuration is invalid.
 */
@SuppressWarnings("serial")
public class UnexpectedGroupPropertiesException extends InvalidPropertiesException {

	/*package-only*/ UnexpectedGroupPropertiesException(String message) {
		super(message);
	}
}