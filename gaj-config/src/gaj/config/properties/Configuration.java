/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.properties;

/*
 * Defines the base configuration interface.
 */
public interface Configuration<T> {

	/**
	 * Distinguishes between a container of key/value properties
	 * and a container of group/configuration properties.
	 * 
	 * @return A value of true (or false) if the configuration
	 * is (or is not) grouped.
	 */
	boolean isGrouped();

	/**
	 * Tests for the existence of any defined properties.
	 * 
	 * @return A value of false (or true) if the configuration
	 * does (or does not) contain any properties.
	 */
	boolean isEmpty();

	/**
	 * Adds all properties from the given configuration
	 * to the current configuration.
	 * 
	 * @param config - The given configuration.
	 * @throws InvalidPropertiesException If the type of configuration
	 * to be added is not supported.
	 */
	void add(Configuration<T> config) throws InvalidPropertiesException;

	/**
	 * Removes all properties from the configuration.
	 */
	void clear();

}