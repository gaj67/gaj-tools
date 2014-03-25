package gaj.config.properties;

/**
 * An interface designed to allow dynamic relabelling of
 * group names when reading in a configuration file.
 */
public interface GroupNameTranslator {

	/**
	 * Obtains a unique symbol to reference an un-named
	 * group of properties.
	 * 
	 * @return The global group name.
	 */
	String getGlobalGroupName();

	/**
	 * Turns a group name string into an array of group names,
	 * with each group considered as being nested inside the
	 * previous group.
	 * 
	 * @param groupName - The string name of a group of properties.
	 * @return An array of nested group names.
	 */
	String[] getNestedGroupNames(String groupName);

}