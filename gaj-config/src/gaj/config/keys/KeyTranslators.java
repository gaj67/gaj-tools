/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.keys;


/**
 * This module deals with translators of the names of configurable properties.
 */
public abstract class KeyTranslators {

	private KeyTranslators() {}

	/**
	 * Creates a key-name translator that simply
	 * copies the names of fields and methods.
	 * 
	 * @return The key translator.
	 */
	public static KeyTranslator newSimpleTranslator() {
		return new SimpleKeyTranslator();
	}

	/**
	 * Creates a key-name translator that interprets
	 * the names of fields and methods according to
	 * changes in character case, and returns dot-separated
	 * names.
	 * 
	 * @return The key translator.
	 */
	public static KeyTranslator newCamelCaseTranslator() {
		return new CamelCaseKeyTranslator(".");
	}

	/**
	 * Creates a key-name translator that interprets
	 * the names of fields and methods according to
	 * changes in character case, and joins
	 * the interpreted pieces with the given separator.
	 * 
	 * @param separator - The separator string.
	 * @return The key translator.
	 */
	public static KeyTranslator newCamelCaseTranslator(String separator) {
		return new CamelCaseKeyTranslator(separator);
	}

}
