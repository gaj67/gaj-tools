/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.declaration;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * An interface designed for guessing the key-name of a property
 * from the name of the annotated field or method.
 */
public interface KeyTranslator {

	/**
	 * Provides context to the key-name translator about
	 * the source of the method provided.
	 */
	public static enum Context {
		/** Translate key-name from the name of a getter method. */
		GETTER, 
		/** Translate key-name from the name of a setter method. */
		SETTER  
	}

	/**
	 * Determines a new key-name for a property based upon the
	 * name provided via annotation.
	 * 
	 * @param name - The name to translate.
	 * @return A translated key-name.
	 */
	String getKey(String name);

	/**
	 * Determines the key-name for a property based upon the
	 * annotated field.
	 * 
	 * @param field - The field to translate.
	 * @return A translated key-name.
	 */
	String getKey(Field field);

	/**
	 * Determines the key-name for a property based upon the
	 * annotated field.
	 * 
	 * @param context - An indicator of whether the
	 * method is annotated as a getter or a setter.
	 * @param method - The method to translate.
	 * @return A translated key-name.
	 */
	String getKey(Context context, Method method);

}