/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.declaration;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Summarises the information about a single
 * configurable property, obtained from one or more 
 * property annotations on fields or methods of a class.
 */
public interface Declaration {

	/**
	 * Obtains the type of property being declared.
	 * 
	 * @return The class of the underlying property.
	 */
	Class<?> getType();

	/**
	 * Obtains the optional configuration key-name string associated with the property.
	 * 
	 * @return The specified key-name,
	 * or a null value if a key has not been specified.
	 */
	/*@Nullable*/ String getKey();

	/**
	 * Obtains the required status of the property.
	 * If true, a value for the property is required to be
	 * specified in the external configuration; 
	 * otherwise a value is optional.
	 * 
	 * @return A value of true (or false) if the property is (or is not)
	 * required.
	 */
	boolean isRequired();

	/**
	 * @return A value of true (or false) if the property does (or does not)
	 * have a supplied default value.
	 */
	boolean hasDefault();

	/**
	 * Obtains the default value for the property,
	 * serialised as a string.
	 * 
	 * @return The supplied default value for the property,
	 * or a value of null if there is no default.
	 */
	/*@Nullable*/ String getValue();

	/**
	 * Obtains the optional field associated with the property.
	 * 
	 * @return The property field, or a value of null if the property
	 * is not associated with a field.
	 */
	/*@Nullable*/ Field getField();

	/**
	 * Obtains the optional setter method for the property.
	 * 
	 * @return The setter method, or a value of null if the property
	 * does not have an associated setter method.
	 */
	/*@Nullable*/ Method getSetter();

	/**
	 * Obtains the optional getter method for the property.
	 * 
	 * @return The setter method, or a value of null if the property
	 * does not have an associated getter method.
	 */
	/*@Nullable*/ Method getGetter();

	/**
	 * Provides an {@link InvalidDeclarationException}.
	 * 
	 * @param message - The error message.
	 * @return The exception.
	 */
	default InvalidDeclarationException failure(String message) {
		return new InvalidDeclarationException(message);
	}

}