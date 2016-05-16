/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.configurators;

/**
 * An exception thrown if any aspect of the content of a configuration is invalid.
 */
@SuppressWarnings("serial")
public class InvalidConfigurationException extends RuntimeException {

	/*package-only*/ InvalidConfigurationException(String message) {
		super(message);
	}

	/*package-only*/  InvalidConfigurationException(Throwable t) {
		super(t);
	}

}