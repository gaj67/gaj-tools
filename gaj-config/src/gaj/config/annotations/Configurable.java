/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as being configurable.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Configurable {

	/**
	 * Indicates that an explicit global key-name
	 * has not been specified by the user.
	 */
	public static String DEFAULT_KEY = "--NO-NAME-SPECIFIED--";

	/**
	 * @return The configuration key-name string corresponding to the global name-space of the class,
	 * or the DEFAULT_KEY if no such value is specified.
	 */
	String value() default DEFAULT_KEY;

}
