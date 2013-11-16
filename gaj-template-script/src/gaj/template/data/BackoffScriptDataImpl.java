/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is designed both to protect externally defined properties from
 * being changed during template embedding, and to allow missing properties to be searched for in
 * some pre-defined back-off order.
 * <p/>
 * It does this by offering a clean, local ScriptData object for setting
 * properties, and retains the global ScriptData objects for getting
 * properties not found in the local store.
 */
/*package-private*/ class BackoffScriptDataImpl extends ScriptDataImpl {

    private final ScriptData[] backoff;

    /**
     * Keeps the given global properties for back-off, and offers an
     * initially-empty local properties store as the main point of contact.
     *
     * @param backoff - One or more EmbeddingData objects, which are not altered
     * during use.
     */
    BackoffScriptDataImpl(ScriptData... backoff) {
        super();
        this.backoff = backoff;
    }

    @Override
    public boolean hasProperty(String key) {
        // Check local store.
        if (super.hasProperty(key)) {
            return true;
        }
        // Check global stores in order.
        for (ScriptData data : backoff) {
            if (data.hasProperty(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object getProperty(String key) {
        // Check local store.
        if (super.hasProperty(key)) {
            return super.getProperty(key); // Could still be null, e.g. if @@set(variable) used in template.
        }
        // Check global stores in order.
        for (ScriptData data : backoff) {
            if (data.hasProperty(key)) {
                return data.getProperty(key);
            }
        }
        return null;
    }

    @Override
    public Object getProperty(String key, Object defaultValue) {
        Object value = getProperty(key);
        return (value != null) ? value : defaultValue;
    }

    @Override
    public Collection<String> getPropertyNames() {
        Set<String> keys = new HashSet<String>();
        keys.addAll(super.getPropertyNames());
        for (ScriptData data : backoff) {
            keys.addAll(data.getPropertyNames());
        }
        return Collections.unmodifiableSet(keys);
    }

    @Override
    public void removeProperty(String key) {
        super.removeProperty(key);        // Remove local property.
        if (hasProperty(key))             // Global property is now visible.
            throw new ScriptDataException("Cannot remove global property: " + key);
    }

}
