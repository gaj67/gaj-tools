/*
 * (c) Geoff Jarrad, 2016.
 */
package gaj.config.keys;

/**
 * An exception thrown if any aspect of translating a key fails.
 */
@SuppressWarnings("serial")
public class KeyTranslationException extends RuntimeException {

	/*package-private*/ KeyTranslationException(String message) {
		super(message);
	}

}