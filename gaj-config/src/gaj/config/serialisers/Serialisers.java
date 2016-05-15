/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serialisers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * This factory handles interfaces and managers for serialising objects
 * into strings, and deserialising strings into objects.
 * <p/>Each serialiser has a specified type, so the type information is
 * <b>not</b> added to the serialisation.
 */
public abstract class Serialisers {

	@SuppressWarnings("serial")
	private static final Map<Class<?>, Class<? extends BaseSerialiser<?>>> SERIALISERS = 
	new HashMap<Class<?>, Class<? extends BaseSerialiser<?>>>() {{
		put(Boolean.class, BooleanSerialiser.class);
		put(Double.class, DoubleSerialiser.class);
		put(Float.class, FloatSerialiser.class);
		put(Integer.class, IntegerSerialiser.class);
		put(Long.class, LongSerialiser.class);
		put(Short.class, ShortSerialiser.class);
		put(String.class, StringSerialiser.class);
	}};

	private Serialisers() {}

	/**
	 * Provides a mapping from built-in data types to their corresponding serialiser classes.
	 * 
	 * @return The type -> serialiser mapping.
	 */
	public static Map<Class<?>, Class<? extends Serialiser<?>>> getSerialiserClasses() {
		return Collections.unmodifiableMap(SERIALISERS);
	}

	/**
	 * Determines an appropriate serialiser class for the given data class.
	 *
	 * @param dataClass - The type of object to be serialised.
	 * @return An appropriate serialiser class, or a value of null if
	 * the object type is not handled.
	 */
	@SuppressWarnings("unchecked")
	public static <T> /*@Nullable*/ Class<? extends Serialiser<T>> getSerialiserClass(Class<T> dataClass) {
		return (Class<? extends Serialiser<T>>) SERIALISERS.get(dataClass);
	}

	public static Class<? extends Serialiser<String>> getStringSerialiserClass() {
		return StringSerialiser.class;
	}

	public static Class<? extends Serialiser<Integer>> getIntegerSerialiserClass() {
		return IntegerSerialiser.class;
	}

	public static Class<? extends Serialiser<Boolean>> getBooleanSerialiserClass() {
		return BooleanSerialiser.class;
	}

	/**
	 * Creates a new instance of a serialiser for the given data class.
	 *
	 * @param dataType - The type of object to be serialised.
	 * @param nullMarker - The serialisation of a null value.
	 * @return An appropriate serialiser instance, or a value of null if
	 * the data-type is not handled.
	 * @throws IllegalStateException If the serialiser cannot be instantiated.
	 */
	@SuppressWarnings("unchecked")
	public static <T> /*@Nullable*/ Serialiser<T> newSerialiser(Class<T> dataType, String nullMarker) {
		Class<? extends BaseSerialiser<?>> klass = SERIALISERS.get(dataType);
		if (klass == null) return null;
		try {
			Constructor<? extends BaseSerialiser<?>> serialiser = klass.getDeclaredConstructor(String.class);
			return (Serialiser<T>) serialiser.newInstance(nullMarker);
		} catch (ClassCastException | InstantiationException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Creates a new instance of a serialiser for the given data class.
	 *
	 * @param dataType - The type of object to be serialised.
	 * @return An appropriate serialiser instance, or a value of null if
	 * the data-type is not handled.
	 * @throws IllegalStateException If the serialiser cannot be instantiated.
	 */
	@SuppressWarnings("unchecked")
	public static <T> /*@Nullable*/ Serialiser<T> newSerialiser(Class<T> dataType) {
		Class<? extends BaseSerialiser<?>> klass = SERIALISERS.get(dataType);
		if (klass == null) return null;
		try {
			return (Serialiser<T>) klass.newInstance();
		} catch (ClassCastException | InstantiationException | SecurityException | IllegalAccessException | IllegalArgumentException e) {
			throw new IllegalStateException(e);
		}
	}

}