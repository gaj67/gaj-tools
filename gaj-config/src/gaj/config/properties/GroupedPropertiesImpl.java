/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements a simple Properties container via a map.
 */
/*package-private*/ class GroupedPropertiesImpl<T> implements GroupedProperties<T> {

	/**
	 * Stores the actual property key/value pairs.
	 */
    private final Map<String, Configuration<T>> groups = new HashMap<>();
    /**
     * Maintains the key-set in a form allowing property modification during iteration.
     * Recomputed as necessary.
     */
    private /*@Nullable*/ Collection<String> keys = null;

    //========================================================
    // Configuration interface.
	@Override
	public boolean isGrouped() {
		return true;
	}

	@Override
	public boolean isEmpty() {
        return groups.isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void add(Configuration<T> config) {
		if (!config.isGrouped())
			throw new InvalidPropertiesException("Cannot add a non-grouped configuration to a grouped one");
		GroupedProperties<T> properties = (GroupedProperties<T>) config;
        for (String groupName : properties.getGroupNames()) {
        	/*@Nullable*/ Configuration<? extends T> group = groups.get(groupName);
        	if (group == null) {
        		groups.put(groupName, properties.get(groupName));
        	} else {
        		Configuration<T> input = properties.get(groupName);
        		if (input != null) {
        			try {
        				((Configuration<T>)group).add(input);
        			} catch (ClassCastException e) {
        				throw new InvalidPropertiesException("Trying to merge incompatible configurations for group " + groupName);
        			}
        		}
        	}
        }
        keys = null;
	}

	@Override
	public void clear() {
        groups.clear();
        keys = null;
	}

    //========================================================
    // GroupedProperties interface.
	@Override
	public Collection<String> getGroupNames() {
		if (keys == null)
			keys = Collections.unmodifiableCollection(new ArrayList<>(groups.keySet()));
		return keys;
	}

	@Override
	public boolean has(String groupName) {
        return groups.containsKey(groupName);
	}

	@Override
	public /*@Nullable*/ Configuration<T> get(String groupName, boolean... isGrouped) {
		if (isGrouped.length > 1)
			throw new IllegalArgumentException("Too many flags");
		/*@Nullable*/ Configuration<T> group = groups.get(groupName);
		if (group != null || isGrouped.length == 0) return group;
		group = isGrouped[0] ? new GroupedPropertiesImpl<T>() : new PropertiesImpl<T>();
		groups.put(groupName, group);
		return group;
	}

	@Override
	public void set(String groupName, Configuration<T> config) {
        groups.put(groupName, config);
	}

	@Override
	public void remove(String groupName) {
        groups.remove(groupName);
	}

}
