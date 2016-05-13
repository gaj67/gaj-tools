/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.multiserialisers;

import gaj.config.serialisers.BaseSerialiser;
import gaj.config.serialisers.BooleanSerialiser;
import gaj.config.serialisers.DoubleSerialiser;
import gaj.config.serialisers.FloatSerialiser;
import gaj.config.serialisers.IntegerSerialiser;
import gaj.config.serialisers.InvalidSerialisationException;
import gaj.config.serialisers.LongSerialiser;
import gaj.config.serialisers.Serialiser;
import gaj.config.serialisers.ShortSerialiser;
import gaj.config.serialisers.StringSerialiser;

import java.util.HashMap;
import java.util.Map;



/**
 * This factory handles interfaces and managers for serialising objects
 * into strings, and deserialising strings into objects.
 */
public class SerialiserFactory {

	@SuppressWarnings("serial")
	private static final Map<Class<?>, Class<? extends Serialiser<?>>> SERIALISERS = 
		new HashMap<Class<?>, Class<? extends Serialiser<?>>>() {{
			put(Boolean.class, BooleanSerialiser.class);
			put(Double.class, DoubleSerialiser.class);
			put(Float.class, FloatSerialiser.class);
			put(Integer.class, IntegerSerialiser.class);
			put(Long.class, LongSerialiser.class);
			put(Short.class, ShortSerialiser.class);
			put(String.class, StringSerialiser.class);
		}};

	private final MultiSerialiserConfigImpl config = new MultiSerialiserConfigImpl();

	private SerialiserFactory() {}

	/**
	 * Creates a Serialiser factory with the default configuration.
	 *
	 * @return A default factory instance.
	 */
	public static SerialiserFactory newInstance() {
		return new SerialiserFactory();
	}

	/**
	 * Specifies the special serialisation markers.
	 */
	public static enum Marker {
		/**
		 * Indicates the start of a type marker.
		 */
		Prefix,
		/**
		 * Indicates the end of a type marker.
		 */
		Suffix,
		/**
		 * Indicates the marker for a null object.
		 */
		Null
	}

	/**
	 * Creates a Serialiser factory with the specified configuration.
	 *
	 * @param config - A sequence of Marker/String pairs.
	 * @return A configured factory instance.
	 * @throws IllegalArgumentException If the configuration is invalid.
	 */
	public static SerialiserFactory newInstance(Object... config) {
		final int len = config.length;
		if (len % 2 != 0)
			throw new IllegalArgumentException("Mismatched key/value pairing");
		SerialiserFactory factory = new SerialiserFactory();
		Marker key = null;
		for (int i = 0; i < len; i++) {
			if (i % 2 == 0) { // Key.
				if (!(config[i] instanceof Marker))
					throw new IllegalArgumentException("Expected Marker key, got " + config[i]);
				key = (Marker) config[i];
			} else { // Value.
				if (!(config[i] instanceof String))
					throw new IllegalArgumentException("Expected String value, got " + config[i]);
				switch (key) {
				case Null:
					factory.config.setNullMarker((String) config[i]);
					break;
				case Prefix:
					factory.config.setTypePrefix((String) config[i]);
					break;
				case Suffix:
					factory.config.setTypeSuffix((String) config[i]);
					break;
				default:
					throw new IllegalArgumentException("Unhandled Marker key: " + key);
				}
			}
		}
		return factory;
	}

	/**
	 * Obtains the serialiser configuration in use by the factory.
	 *
	 * @return An immutable configuration object.
	 */
	public MultiSerialiserConfig getConfiguration() {
		return config;
	}

	/**
	 * Determines an appropriate serialiser class for the given data class.
	 *
	 * @param dataClass - The type of object to be serialised.
	 * @return An appropriate serialiser class, or a value of null if
	 * the object type is not handled.
	 */
	@SuppressWarnings("unchecked")
	public <T> /*@Nullable*/ Class<? extends Serialiser<T>> getSerialiser(Class<T> dataClass) {
		return (Class<? extends Serialiser<T>>) SERIALISERS.get(dataClass);
	}

	/**
	 * Creates a new instance of a serialiser for the given data class.
	 * If this serialiser is configurable, then it will be configured with
	 * the factory configuration.
	 *
	 * @param dataClass - The type of object to be serialised.
	 * @return An appropriate serialiser instance, or a value of null if
	 * the object type is not handled.
	 * @throws InvalidSerialisationException If the serialiser cannot
	 * be instantiated.
	 */
	public <T> /*@Nullable*/ Serialiser<T> newSerialiser(Class<T> dataClass) {
		@SuppressWarnings("unchecked")
		Class<? extends Serialiser<T>> klass = (Class<? extends Serialiser<T>>) SERIALISERS.get(dataClass);
		if (klass == null) return null;
		try {
			Serialiser<T> serialiser = klass.newInstance();
			if (serialiser instanceof ConfigurableSerialiser<?>)
				((ConfigurableSerialiser<?>) serialiser).configure(config);
			return serialiser;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new InvalidSerialisationException(e);
		}
	}

	/**
	 * Creates a multi-serialiser, optionally pre-configured 
	 * with standard serialisers for the String, 
	 * Boolean and various numerical classes.
	 *
	 * @param preConfigure - A flag indicating whether
	 * (true) or not (false) to pre-configure
	 * the multi-serialiser. 
	 * @return A multi-serialiser manager.
	 */
	public MultiSerialiser newMultiSerialiser(boolean preConfigure) {
		return new MultiSerialiserImpl(config, preConfigure);
	}

}