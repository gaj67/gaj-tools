/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.declaration;

import gaj.config.annotations.Annotations;
import gaj.config.keys.KeyTranslator;
import gaj.config.keys.KeyTranslators;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * This module deals with the declaration of configurable properties for a class.
 */
public class DeclarationManager {

	private final KeyTranslator translator;

	/**
	 * Binds the manager instance to the given key-name translator.
	 * 
	 * @param translator - The key-name translator.
	 */
	private DeclarationManager(/*@Nullable*/ KeyTranslator translator) {
		this.translator = translator;
	}

	/**
	 * Creates a manager instance with a simple key-name translator.
	 * 
	 * @return An unconfigured manager instance.
	 */
	public static DeclarationManager newInstance() {
		return new DeclarationManager(KeyTranslators.newSimpleTranslator());
	}

	/**
	 * Creates a manager instance bound to the given key-name translator.
	 * 
	 * @param translator - The key-name translator, or a value of null to
	 * prevent translation.
	 * @return A configured manager instance.
	 */
	public static DeclarationManager newInstance(/*@Nullable*/ KeyTranslator translator) {
		return new DeclarationManager(translator);
	}

	/**
	 * Creates a manager instance bound to a camel-case key-name translator.
	 * 
	 * @param separator - The key separator symbol.
	 * @return A configured manager instance.
	 */
	public static DeclarationManager newInstance(String separator) {
		return new DeclarationManager(KeyTranslators.newCamelCaseTranslator(separator));
	}

	/**
	 * Extracts the translated property declaration on a field.
	 * 
	 * @param field - The field to test.
	 * @return A declaration instance summarising the property annotations
	 * on the field, or the null value if there are no such annotations.
	 * @throws InvalidDeclarationException If the property annotations are inconsistent.
	 */
	public /*@Nullable*/ Declaration getDeclaration(final Field field) {
		return Declarations.getDeclaration(field, translator);
	}

	/**
	 * Extracts the translated property declaration on a method.
	 * 
	 * @param method - The method to test.
	 * @return A declaration instance summarising the property annotations
	 * on the method, or the null value if there are no such annotations.
	 * @throws InvalidDeclarationException If the property annotations are inconsistent.
	 */
	public /*@Nullable*/ Declaration getDeclaration(final Method method) {
		return Declarations.getDeclaration(method, translator);
	}

	/**
	 * Obtains a collection of unmerged property declarations from the
	 * given class, using key-name translation if necessary. 
	 * The collection will be empty if the class
	 * has no declared properties or is not configurable.
	 * 
	 * @param klass - An allegedly configurable class,
	 *  supposedly containing property declarations.
	 * @return A collection of unmerged property declarations.
	 * @throws InvalidDeclarationException If any property
	 * is marked with inconsistent settings.
	 */
	public Collection<Declaration> getDeclarations(Class<?> klass) {
		return Declarations.getDeclarations(klass, translator);
	}

	/**
	 * Obtains the merged property declarations from the
	 * given class, using key-name translation if
	 * necessary. 
	 * The result will be empty if the class
	 * has no declared properties or is not configurable.
	 * 
	 * @param klass - An allegedly configurable class,
	 *  supposedly containing property declarations.
	 * @param useGlobalNamespace - Indicates whether (true) or not (false) to prepend 
	 * the local property key-names with the global key-name. 
	 * 
	 * @return A map of merged property declarations.
	 * @throws InvalidDeclarationException If any property
	 * is marked with inconsistent settings.
	 */
	public DeclarationMap getDeclarationMap(Class<?> klass, boolean useGlobalNamespace) {
		Map<String, Declaration> declarations = Declarations.mergeDeclarationsByKey(getDeclarations(klass));
		if (useGlobalNamespace) {
			String globalKey = Annotations.getKeyName(klass);
			if (translator != null) {
				if (globalKey == null) globalKey = translator.guessGlobalKey(klass);
				if (globalKey != null) globalKey = translator.translateGlobalKey(globalKey);
			}
			if (globalKey != null) {
				Declarations.globaliseKeys(declarations, globalKey);
			}
		}
		return new DeclarationMapImpl(declarations);
	}

}
