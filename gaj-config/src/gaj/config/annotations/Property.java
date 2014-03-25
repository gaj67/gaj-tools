/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field as being a property.
 * <p/>Optionally specifies the user-configuration key-name
 * associated with the property.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Property {

	/**
	 * Indicates that an explicit property key-name
	 * has not been specified by the user, and
	 * will instead be automatically deduced
	 * from the name of the annotated field.
	 */
	public static String DEFAULT_KEY = "--DEFAULT-KEY--";

	/**
	 * @return The configuration key-name string corresponding to the
	 * required property. If not specified, defaults to the
	 * DEFAULT_KEY value.
	 */
	String value() default DEFAULT_KEY;
}