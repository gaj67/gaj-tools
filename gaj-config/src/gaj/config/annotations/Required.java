/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field or method as having a required 
 * property.
 * <p/>Note: It is a contradiction for a property
 * to both be required <b>and</b> have a default value.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Required { }