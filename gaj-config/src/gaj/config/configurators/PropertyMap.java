package gaj.config.configurators;

import gaj.config.multiserialisers.MultiSerialiser;
import gaj.config.serialisers.Serialiser;


/**
 * Provides an interface for interrogating the property values to be set on one or more classes.
 * <p/>It is assumed that these values remain in serialised string form, and will be deserialised as 
 * necessary by an external {@link Serialiser} or {@link MultiSerialiser}.
 */
public interface PropertyMap {

	/**
	 * Obtains the number of properties values in the configuration.
	 * 
	 * @return The number of properties.
	 */
	int numProperties();

	/**
	 * Obtains the key-names of all properties.
	 * 
	 * @return An iterable over the key-names.
	 */
	Iterable<String> getKeys();

	/**
	 * Obtains the serialised property value associated with the given key-name.
	 * 
	 * @param key - The key-name.
	 * @return The serialised property value, or a value of null
	 * if there is no property value for that key.
	 */
	/*@Nullable*/ String getValue(String key);

}
