package gaj.text.handler;

import java.util.Map;

/**
 * Specifies an arbitrary event with optional properties.
 */
public interface Event<T,V> {

	/**
	 * Obtains the optional event type.
	 * 
	 * @return The event type, or a value of null if it has no type.
	 */
	/*@Nullable*/ T getType();

	/**
	 * Obtains the optional event label.
	 * 
	 * @return The event label, or a value of null if it has no label.
	 */
	/*@Nullable*/ String getLabel();
	
	/**
	 * Determines whether or not the event has the given property.
	 * If the given value is null, then a match occurs only if the event does not
	 * have a property of that name, or if its value is also null. Otherwise, a match
	 * only occurs if the event has a property of the given name with the given value.
	 * 
	 * @param name - The property key.
	 * @param value - The (possibly null) property value.
	 * @return A value of true (or false) if the event does (or does not) have the given property.
	 */
	boolean hasProperty(String name, /*@Nullable*/ V value);
	
	/**
	 * Determines whether or not the event has all of the given properties.
	 * If a given property value is null, then a match occurs only if the event does not
	 * have a property of that name, or if its value is also null. Otherwise, a match
	 * only occurs if the event has a property of the given name with the given value.
	 * 
	 * @param keyValue - A map of property name and value entries.
	 * @return A value of true (or false) if the event does (or does not) have the given properties.
	 */
	boolean hasProperties(Map<String,V> keyValues);
	
	/**
	 * Obtains the named property of the event.
	 * 
	 * @param name - The property key.
	 * @return The event property value, or a value of null if there is no such property.
	 */
	/*@Nullable*/ V getProperty(String name);
	
	/**
	 * Obtains a map of the event properties.
	 * 
	 * @return The event properties map.
	 */
	Map<String,V> getProperties();
	
}
