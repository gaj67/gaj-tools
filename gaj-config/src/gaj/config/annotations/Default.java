/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field or method with a default property value.
 * <p/>Note: It is a contradiction for a property
 * to both have a default value <b>and</b> be required.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Default {
	/**
	 * Obtains a string serialisation of the default value
	 * of a configurable property.
	 * 
	 * @return The default value.
	 */
	String value();
}