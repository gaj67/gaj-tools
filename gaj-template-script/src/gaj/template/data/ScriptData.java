/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.data;

import java.util.Collection;

/**
 * Encapsulates a collection of key/value properties, where each key is a String, and each value is an Object (possibly null).
 */
public interface ScriptData {

    /**
     * Sets the value for the given property.
     *
     * @param key - The string name of the property.
     * @param value - The value of the property. Note that a value of null is
     * equivalent to the property being undefined.
     */
    public void setProperty(String key, Object value);

    /**
     * Obtains the value for the given property.
     *
     * @param key - The string name of the property.
     * @return The value of the property, or null if it is not defined.
     */
    public Object getProperty(String key);

    /**
     * Obtains the defined value for the given property or its default value.
     *
     * @param key - The string name of the property.
     * @param defaultValue - A default value for the property.
     * @return The value of the property, or the default value if the property
     * is not defined.
     */
    public Object getProperty(String key, Object defaultValue);

    /**
     * Determines whether or not a property with the given key name exists. This
     * test is independent of any value of the property (including null).
     *
     * @param key - The string name of the property.
     * @return A value of true (or false) if the named property does (or does
     * not) exist, regardless of any property value.
     */
    public boolean hasProperty(String key);

    /**
     * Removes the specified property if it exists.
     *
     * @param key - The string name of the property.
     */
    public void removeProperty(String key);

    /**
     * Obtains the entire collection of property key names.
     *
     * @return An unmodifiable collection of property keys.
     */
    public Collection<String> getPropertyNames();

}