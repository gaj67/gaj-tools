/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.keys;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * An interface designed for guessing the key-name of a property
 * from the name of the annotated field or method.
 */
public interface KeyTranslator {

	/**
	 * Determines a new key-name for a class based upon the
	 * name provided via annotation or guessed from the class name.
	 * <p/>This method should append a name-space separator symbol if one is required.
	 * 
	 * @param name - The name to translate.
	 * @return A translated key-name.
	 */
	String translateGlobalKey(String name);

	/**
	 * Determines a new key-name for a property based upon the
	 * name provided via annotation or guessed from a field or method.
	 * 
	 * @param name - The name to translate.
	 * @return A translated key-name.
	 */
	String translateKey(String name);

	/**
	 * Deduces the global key-name for a configurable class without an explicit key-name.
	 * 
	 * @param klass - The class. 
	 * @return A global key-name, or a value of null if no such name can be determined.
	 */
	/*@Nullable*/ String guessGlobalKey(Class<?> klass);

	/**
	 * Deduces the key-name for a property based upon an
	 * annotated field without an explicit key-name.
	 * 
	 * @param field - The field.
	 * @return A property key-name.
	 */
	String guessPropertyKey(Field field);

	/**
	 * Deduces the key-name for a property based upon an
	 * annotated getter method without an explicit key-name.
	 * 
	 * @param method - The getter method.
	 * @return A property key-name.
	 */
	String guessGetterKey(Method method);

	/**
	 * Deduces the key-name for a property based upon an
	 * annotated setter method without an explicit key-name.
	 * 
	 * @param method - The setter method.
	 * @return A property key-name.
	 */
	String guessSetterKey(Method method);

	/**
	 * Provides a {@link KeyTranslationException} when translation fails.
	 * 
	 * @param message - The error message.
	 * @return The exception.
	 */
	default KeyTranslationException failure(String message) {
		return new KeyTranslationException(message);
	}

}