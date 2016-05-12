/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.annotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * This module deals with annotations for configurable class properties.
 */
public class Annotations {

	/**
	 * Determines if a class is configurable.
	 * @param klass - The class to test.
	 * @return A value of true (or false) if the class is (or is not)
	 * marked as being configurable.
	 */
	public static boolean isConfigurable(Class<?> klass) {
		return klass.isAnnotationPresent(Configurable.class);
	}

	/**
	 * Determines if the class should be instantiated as a singleton.
	 * @param klass - The class to test.
	 * @return A value of true (or false) if the class
	 * is (or is not) to be instantiated as a singleton.
	 */
	public static boolean isSingleton(Class<?> klass) {
		return klass.isAnnotationPresent(Singleton.class);
	}

	/**
	 * Determines if a field represents a property.
	 * @param  - The field to test.
	 * @return A value of true (or false) if the field is (or is not)
	 * marked as being a property.
	 */
	public static boolean isProperty(Field field) {
		return field.isAnnotationPresent(Property.class);
	}

	/**
	 * Obtains the optional, user-specified key-name of the property.
	 * 
	 * @param  - The field to test.
	 * @return The string key-name of the property if it is specified,
	 * or a null value if it is not specified or
	 * if the field is not a property.
	 */
	public static /*@Nullable*/ String getKeyName(Field field) {
		Property anno = field.getAnnotation(Property.class);
		if (anno == null) return null;
		String key = anno.value();
		return Property.DEFAULT_KEY.equals(key) ? null : key;
	}

	/**
	 * Determines if a field is a required property. 
	 * By default, a property value is optional. 
	 * If required, then a value must
	 * be specified by external configuration. 
	 * 
	 * @param  - The field to test.
	 * @return A value of true (or false) if the field is (or is not)
	 * marked as being a required property.
	 */
	public static boolean isRequired(Field field) {
		return field.isAnnotationPresent(Required.class);
	}

	/**
	 * Determines if a field has a default property value.
	 * 
	 * @param  - The field to test.
	 * @return A value of true (or false) if the field is (or is not)
	 * marked as having a default property value.
	 */
	public static boolean hasDefault(Field field) {
		return field.isAnnotationPresent(Default.class);
	}

	/**
	 * Obtains the default serialised value of the property
	 * associated with the given field.
	 * 
	 * @param  - The field to test.
	 * @return The string representation of the default value
	 * of the associated property, or a null value if the default
	 * is not specified.
	 */
	public static /*@Nullable*/ String getDefaultValue(Field field) {
		Default anno = field.getAnnotation(Default.class);
		return (anno == null) ? null : anno.value();
	}

	/**
	 * Determines if a method represents a simple property getter.
	 * 
	 * @param  - The method to test.
	 * @return A value of true (or false) if the method is (or is not)
	 * marked as being a property getter, has no arguments,
	 * and has a non-void return type.
	 */
	public static boolean isGetter(Method method) {
		return method.isAnnotationPresent(Getter.class)
				&& method.getReturnType() != Void.class 
				&& method.getParameterTypes().length == 0;
	}

	/**
	 * Determines if a method represents a simple property setter.
	 * 
	 * @param  - The method to test.
	 * @return A value of true (or false) if the method is (or is not)
	 * marked as being a property setter, and has exactly one argument.
	 */
	public static boolean isSetter(Method method) {
		return method.isAnnotationPresent(Setter.class)
				&& method.getParameterTypes().length == 1;
	}

	/**
	 * Obtains the optional key-name of the property
	 * associated with the getter or setter method.
	 *
	 * @param  - The method to test.
	 * @return The string key-name of the property if it is specified,
	 * or a null value if it is not specified or
	 * if the method is not a property getter or setter.
	 */
	public static /*@Nullable*/ String getKeyName(Method method) {
		Getter getter = method.getAnnotation(Getter.class);
		if (getter != null) {
			String key = getter.value();
			return Property.DEFAULT_KEY.equals(key) ? null : key;
		}
		Setter setter = method.getAnnotation(Setter.class);
		if (setter == null) return null;
		String key = setter.value();
		return Property.DEFAULT_KEY.equals(key) ? null : key;
	}

	/**
	 * Determines if a method has a required property. 
	 * By default, a property value is optional. 
	 * If required, then a value must
	 * be specified by external configuration. 
	 * 
	 * @param  - The method to test.
	 * @return A value of true (or false) if the method is (or is not)
	 * marked as having a required property.
	 */
	public static boolean isRequired(Method method) {
		return method.isAnnotationPresent(Required.class);
	}

	/**
	 * Determines if a method has a default property value.
	 * 
	 * @param  - The method to test.
	 * @return A value of true (or false) if the method is (or is not)
	 * marked as having a default property value.
	 */
	public static boolean hasDefault(Method method) {
		return method.isAnnotationPresent(Default.class);
	}

	/**
	 * Obtains the optional default value of the property
	 * associated with the given method.
	 *
	 * @param  - The method to test.
	 * @return The string representation of the default value
	 * of the associated property, or a null value if the default
	 * is not specified.
	 */
	public static /*@Nullable*/ String getDefaultValue(Method method) {
		Default anno = method.getAnnotation(Default.class);
		return (anno == null) ? null : anno.value();
	}

}
