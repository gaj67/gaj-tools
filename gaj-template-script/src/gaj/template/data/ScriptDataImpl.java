/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*package-private*/ class ScriptDataImpl implements ScriptData {

    private final Map<String, Object> properties = new HashMap<>();

    /*package-private*/ ScriptDataImpl() {}

    @Override
    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    @Override
    public Object getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public Object getProperty(String key, Object defaultValue) {
        Object value = properties.get(key);
        return (value != null) ? value : defaultValue;
    }

    @Override
    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }

    @Override
    public void removeProperty(String key) {
        properties.remove(key);
    }

    @Override
    public Collection<String> getPropertyNames() {
        return Collections.unmodifiableSet(properties.keySet());
    }
}
