/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.annotations;

import static gaj.config.annotations.Property.DEFAULT_KEY;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as being a simple property setter.
 * <p/>Optionally specifies the user-configuration key-name
 * associated with the property.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Setter {

	/**
	 * @return The configuration key-name string corresponding to the
	 * required property. If not specified, defaults to the
	 * DEFAULT_KEY value.
	 */
	String value() default DEFAULT_KEY;
}