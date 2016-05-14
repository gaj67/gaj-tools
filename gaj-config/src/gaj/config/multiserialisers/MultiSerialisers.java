/*
 * (c) Geoff Jarrad, 2016.
 */
package gaj.config.multiserialisers;

/**
 * This factory handles interfaces and managers for serialising objects
 * into strings, and deserialising strings into objects.
 */
public abstract class MultiSerialisers {

	private MultiSerialisers() {}

	/**
	 * Creates a multi-serialiser configuration.
	 *
	 * @param config - An optional sequence of {@link TypeMarker}/String pairs.
	 * @return A configuration instance.
	 * @throws IllegalArgumentException If the configuration is invalid.
	 */
	public static MultiSerialiserConfig newConfig(Object... config) {
		return new MultiSerialiserConfigImpl(config);
	}

	/**
	 * Creates a multi-serialiser, optionally pre-configured 
	 * with standard serialisers for the String, 
	 * Boolean and various numerical classes.
	 *
	 * @param config - A multi-serialiser configuration.
	 * @param preConfigure - A flag indicating whether
	 * (true) or not (false) to pre-configure 
	 * the multi-serialiser with the built-in serialisers. 
	 * @return A multi-serialiser.
	 */
	public MultiSerialiser newMultiSerialiser(MultiSerialiserConfig config, boolean preConfigure) {
		return new MultiSerialiserImpl(config, preConfigure);
	}

}