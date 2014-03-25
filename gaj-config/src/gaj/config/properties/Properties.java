/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.properties;

import java.util.Collection;

/**
 * Defines a container of user-specified properties.
 */
public interface Properties<T> extends Configuration<T> {

	/**
	 * Lists the defined properties.
	 * <p/>Note: modifying the properties whilst iterating over the keys 
	 * must not cause a ConcurrentModificationException.
	 * 
	 * @return An immutable collection of property key names.
	 */
	Collection<String> getKeys();

	/**
	 * Checks for the existence of a property.
	 * 
	 * @param key - The string name of the property key.
	 * @return A value of true (or false) if the property key
	 * does (or does not) exist in the configuration, even if
	 * the property value is null.
	 */
	boolean has(String key);

	/**
	 * Gets the property value corresponding to the given key.
	 * 
	 * @param key - The property key-name.
	 * @return - The property value (possibly null), or a value of
	 * null if the property does not exist.
	 */
	/*@Nullable*/ T get(String key);

	/**
	 * Sets a property key/value pair.
	 * 
	 * @param key - The property key-name.
	 * @param value - The property value (possibly null).
	 */
	void set(String key, /*@Nullable*/ T value);

	/**
	 * Removes a property from the configuration.
	 * 
	 * @param key - The property key-name.
	 */
	void remove(String key);

}