/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serialisers;

/**
 * Specifies the type information markers used by all ConfigurableSerialiser implementations.
 */
public interface MultiSerialiserConfig {

	/**
	 * Specifies the prefix used to denote the start of the serialisation type.
	 *
	 * @return The prefix symbol.
	 */
	public String getTypePrefix();

	/**
	 * Specifies the suffix used to denote the end of the serialisation type.
	 *
	 * @return The suffix symbol.
	 */
	public String getTypeSuffix();

	/**
	 * Specifies the unique representation of a serialised <tt>null</tt> object.
	 *
	 * @return The symbol denoting null.
	 */
	public String getNullMarker();

}
