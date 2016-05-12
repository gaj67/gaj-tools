/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.declaration;

import gaj.config.keys.KeyTranslator;
import gaj.config.keys.KeyTranslator.MethodContext;

import java.util.Collection;

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
	 * Creates a manager instance without a key-name translator.
	 * 
	 * @return An unconfigured manager instance.
	 */
	public static DeclarationManager newInstance() {
		return new DeclarationManager(null);
	}

	/**
	 * Creates a manager instance bound to the given key-name translator.
	 * 
	 * @param translator - The key-name translator.
	 * @return A configured manager instance.
	 */
	public static DeclarationManager newInstance(KeyTranslator translator) {
		return new DeclarationManager(translator);
	}

	/**
	 * Obtains a collection of merged 
	 * property declarations from the
	 * given class, using key-name translation if
	 * necessary. 
	 * The collection will be empty if the class
	 * has no declared properties.
	 * 
	 * @param klass - An allegedly configurable class,
	 *  supposedly containing property declarations.
	 * @return A collection of property declarations.
	 * @throws InvalidDeclarationException If any property
	 * is marked with inconsistent settings.
	 */
	public Collection<Declaration> getMergedDeclarations(Class<?> klass) {
		Collection<Declaration> declarations = Declarations.getUnmergedDeclarations(klass);
		if (translator != null) {
			for (Declaration declaration : declarations) {
				translateKey(declaration);
			}
		}
		return Declarations.mergeDeclarationsByKey(declarations);
	}

	private void translateKey(Declaration declaration) {
		String name = declaration.getKey();
		if (name != null)
			name = translator.getKey(name);
		else if (declaration.getField() != null)
			name = translator.getKey(declaration.getField());
		else if (declaration.getGetter() != null)
			name = translator.getKey(MethodContext.GETTER, declaration.getGetter());
		else if (declaration.getSetter() != null)
			name = translator.getKey(MethodContext.SETTER, declaration.getSetter());
		else
			throw new InvalidDeclarationException("No annotatetd property specified");
		// Use BeanDec instead of GuardedDec to bypass value checks.
		((BeanDeclaration)declaration).setKey(name);
	}
}
