/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.properties;

import java.util.Collection;

/**
 * Defines a container of grouped configurations, each indexed by group name.
 */
public interface GroupedProperties<T> extends Configuration<T> {

	/**
	 * Specifies the names of all groups in the configuration.
	 * 
	 * @return An unmodifiable collection of group names.
	 * Note that modifying the groups whilst iterating over this
	 * collection must not cause a ConcurrentModificationException.
	 */
	Collection<String> getGroupNames();

	/**
	 * Tests for the existence of a defined group.
	 * 
	 * @param groupName - The string group name.
	 * @return A value of true (or false) if the named group
	 * does (or does not) exist,
	 * even if the group configuration is null or empty.
	 */
	boolean has(String groupName);

	/**
	 * Returns the properties configuration for the given group,
	 * optionally creating it if it does not exist.
	 * 
	 * @param groupName - The string group name.
	 * @param isGrouped - An optional flag controlling group creation. 
	 * If a flag is specified, then the group properties are created
	 * if they do not exist. In addition, a grouped configuration is created if 
	 * the flag is true, otherwise a non-grouped configuration is created.
	 * @return A modifiable collection of properties, or a value of null
	 * if the named group is not found and not created.
	 */
	/*@Nullable*/ Configuration<T> get(String groupName, boolean... isGrouped);

	/**
	 * Sets the configuration of the named grouped to be the given configuration.
	 * 
	 * @param groupName - The string group name.
	 * @param config - A container of user-configured properties.
	 * @throws InvalidPropertiesException if the configuration to be added
	 * is not of the same type as the existing group configuration.
	 */
	void set(String groupName, Configuration<T> config);

	/**
	 * Removes the entire configuration for the given group, which becomes undefined.
	 * 
	 * @param groupName - The string group name.
	 */
	void remove(String groupName);

}