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
/*package-private*/ class PropertiesImpl<T> implements Properties<T> {

	/**
	 * Stores the actual property key/value pairs.
	 */
    private final Map<String, T> properties = new HashMap<>();
    /**
     * Maintains the key-set in a form allowing property modification during iteration.
     * Recomputed as necessary.
     */
    private /*@Nullable*/ Collection<String> keys = null;

    //========================================================
    // Configuration interface.
	@Override
	public boolean isGrouped() {
		return false;
	}

	@Override
	public boolean isEmpty() {
        return properties.isEmpty();
	}

	@Override
	public void add(Configuration<T> config) {
		if (config.isGrouped())
			throw new InvalidPropertiesException("Cannot add a grouped configuration to a non-grouped one");
		Properties<T> properties = (Properties<T>) config;
        for (String key : properties.getKeys())
            this.properties.put(key, properties.get(key));
        keys = null;
	}

	@Override
	public void clear() {
        properties.clear();
        keys = null;
	}

    //========================================================
    // Properties interface.
	@Override
	public Collection<String> getKeys() {
		if (keys == null)
			keys = Collections.unmodifiableCollection(new ArrayList<>(properties.keySet()));
		return keys;
	}

	@Override
	public boolean has(String key) {
        return properties.containsKey(key);
	}

	@Override
	public /*@Nullable*/ T get(String key) {
        return properties.get(key);
	}

	@Override
	public void set(String key, /*@Nullable*/ T value) {
        properties.put(key, value);
	}

	@Override
	public void remove(String key) {
        properties.remove(key);
	}

}
