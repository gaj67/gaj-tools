package gaj.config.properties;

import java.io.File;
import java.io.IOException;

/**
 * Simplifies property management.
 */
public interface PropertiesManager<T> extends Properties<T> {

	/**
	 * Determines if a property is defined, regardless of
	 * the property value. If a named group cannot be reached,
	 * then a value of false is returned.
	 * 
	 * @param groups - An array of nested property-group names.
	 * @param key - The property key-name string.
	 * @return A value of true (or false) if the property
	 * is (or is not) defined.
	 */
	public boolean has(String[] groups, String key);

	/**
	 * Looks up a property by name. If a named group cannot
	 * be reached, then a null value is returned.
	 * 
	 * @param groups - An array of nested property-group names.
	 * @param key - The property key-name string.
	 * @return The property value, or a value of null if the
	 * property is not defined.
	 */
	public /*@Nullable*/ T get(String[] groups, String key);

	/**
	 * Defines a property by name and value. If a named group
	 * does not exist, it will be created. If it is not a
	 * group, it will be converted to one.
	 * 
	 * @param groups - An array of nested property-group names.
	 * @param key - The property key-name string.
	 * @param value - The property value.
	 */
	public void set(String[] groups, String key, T value);

	/**
	 * Removes a named property. If the property does not exist,
	 * or one of the named groups cannot be reached,
	 * then no action is taken.
	 * 
	 * @param groups - An array of nested property-group names.
	 * @param key - The property key-name string.
	 */
	public void remove(String[] groups, String key);

	/**
	 * Accumulates the properties of nested groups, starting
	 * from the outermost group. If a named group cannot be reached,
	 * then the process ends and the properties
	 * accumulated so far are returned (including the
	 * properties of the last reachable group or non-group).
	 * 
	 * @param groups - An array of nested property-group names.
	 * @return A container of accumulated properties.
	 */
	public Properties<T> accumulate(String[] groups);

	/**
	 * Adds properties to the current configuration.
	 * 
	 * @param config - The configuration properties.
	 */
	public void add(Configuration<T> config);

}