/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.declaration;

import gaj.config.annotations.Default;
import gaj.config.annotations.Getter;
import gaj.config.annotations.Property;
import gaj.config.annotations.Required;
import gaj.config.annotations.Setter;
import gaj.config.declaration.KeyTranslator.Context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This module deals with the declaration of configurable properties for a class.
 */
public class DeclarationFactory {

	private final /*@Nullable*/ KeyTranslator translator;

	/**
	 * Binds the factory instance to the given key-name
	 * translator.
	 * 
	 * @param translator - The key-name translator.
	 */
	private DeclarationFactory(/*@Nullable*/ KeyTranslator translator) {
		this.translator = translator;
	}

	/**
	 * Creates a factory instance without a key-name translator.
	 * 
	 * @return An unconfigured factory instance.
	 */
	public static DeclarationFactory newInstance() {
		return new DeclarationFactory(null);
	}

	/**
	 * Creates a factory instance bound to the
	 * given key-name translator.
	 * 
	 * @param translator - The key-name translator.
	 * @return A configured factory instance.
	 */
	public static DeclarationFactory newInstance(KeyTranslator translator) {
		return new DeclarationFactory(translator);
	}

	/**
	 * Extracts the property configuration declaration on a field.
	 * 
	 * @param field - The field to test.
	 * @return A declaration instance summarising the property annotations
	 * on the field, or the null value if there are no such annotations.
	 * @throws InvalidDeclarationException If the property annotations are inconsistent.
	 */
	public static /*@Nullable*/ Declaration getDeclaration(final Field field) {
		Required requiredAnno = field.getAnnotation(Required.class);
		Default defaultAnno = field.getAnnotation(Default.class);
		if (requiredAnno != null && defaultAnno != null)
			throw new InvalidDeclarationException("A property cannot be required and have a default value");
		Property propertyAnno = field.getAnnotation(Property.class);
		if (propertyAnno == null && requiredAnno == null && defaultAnno == null)
			return null; // Not marked with any configuration annotations.
		GuardedDeclaration dec = new GuardedDeclaration();
		dec.setField(field);
		dec.setType(field.getType());
		if (propertyAnno != null)
			dec.setKey(propertyAnno.value());
		if (requiredAnno != null)
			dec.setRequired(true);
		if (defaultAnno != null)
			dec.setDefault(defaultAnno.value());
		return dec;
	}

	/**
	 * Extracts the property configuration declaration on a method.
	 * 
	 * @param method - The method to test.
	 * @return A declaration instance summarising the property annotations
	 * on the method, or the null value if there are no such annotations.
	 * @throws InvalidDeclarationException If the property annotations are inconsistent.
	 */
	public static /*@Nullable*/ Declaration getDeclaration(Method method) {
		Getter getterAnno = method.getAnnotation(Getter.class);
		Setter setterAnno = method.getAnnotation(Setter.class);
		if (getterAnno != null && setterAnno != null)
			throw new InvalidDeclarationException("A single method cannot be both a getter and setter");
		Required requiredAnno = method.getAnnotation(Required.class);
		Default defaultAnno = method.getAnnotation(Default.class);
		if (requiredAnno != null && defaultAnno != null)
			throw new InvalidDeclarationException("A property cannot be required and have a default value");
		if (getterAnno == null && setterAnno == null
				&& requiredAnno == null && defaultAnno == null)
			return null; // Not marked with any configuration annotations.
		GuardedDeclaration dec = new GuardedDeclaration();
		Class<?>[] args = method.getParameterTypes();
		if (getterAnno != null) {
			if (args == null || args.length != 0)
				throw new InvalidDeclarationException("Not a simple getter method: "+method);
			Class<?> retType = method.getReturnType();
			if (retType == null || retType == void.class)
				throw new InvalidDeclarationException("Not a getter method: "+method);
			dec.setType(retType);
			dec.setGetter(method);
			dec.setKey(getterAnno.value());
		} else if (setterAnno != null) {
			if (args == null || args.length != 1)
				throw new InvalidDeclarationException("Not a simple setter method: "+method);
			dec.setType(args[0]);
			dec.setSetter(method);
			dec.setKey(setterAnno.value());
		}
		if (requiredAnno != null)
			dec.setRequired(true);
		else if (defaultAnno != null)
			dec.setDefault(defaultAnno.value());
		return dec;
	}

	/**
	 * Attempts to merge the settings of one or more
	 * property declarations in a consistent fashion.
	 * 
	 * @param declarations - A number of property declarations.
	 * @return The combined property declaration.
	 * @throws InvalidDeclarationException If the given property
	 * declarations have incompatible settings, or if no property
	 * declarations are given.
	 */
	public static Declaration mergeDeclarations(Declaration... declarations)
			throws InvalidDeclarationException
	{
		if (declarations.length == 0)
			throw new InvalidDeclarationException("No property declarations have been given");
		GuardedDeclaration merged = new GuardedDeclaration();
		merged.merge(declarations);
		return merged;
	}

	/**
	 * Obtains a collection of unmerged 
	 * property declarations from the
	 * given class. The collection will be empty if the class
	 * has no declared properties.
	 * 
	 * @param klass - An allegedly configurable class,
	 *  supposedly containing property declarations.
	 * @return A collection of property declarations.
	 * @throws InvalidDeclarationException If any property
	 * is marked with inconsistent settings.
	 */
	public static Collection<Declaration> getUnmergedDeclarations(Class<?> klass)
			throws InvalidDeclarationException
	{
		List<Declaration> declarations = new ArrayList<Declaration>();
		for (Field field : klass.getFields()) {
			/*@Nullable*/ Declaration dec = getDeclaration(field);
			if (dec != null)
				declarations.add(dec);
		}
		for (Method method : klass.getMethods()) {
			/*@Nullable*/ Declaration dec = getDeclaration(method);
			if (dec != null)
				declarations.add(dec);
		}
		return declarations;
	}

	/**
	 * Merges multiple property declarations with
	 * the same key-name string into a single declaration.
	 * 
	 * @param declarations - A collection of property declarations.
	 * @return A collection of merged property declarations.
	 * @throws InvalidDeclarationException If any property
	 * does not have a key-name, or is otherwise marked with
	 * inconsistent settings.
	 */
	public static Collection<Declaration> mergeDeclarationsByKey(
			Collection<Declaration> declarations)
					throws InvalidDeclarationException
	{
		Map<String,Declaration> mergedDeclarations = new HashMap<String,Declaration>();
		for (Declaration bareDeclaration : declarations) {
			String key = bareDeclaration.getKey();
			if (key == null)
				throw new InvalidDeclarationException("Cannot merge property declarations with a null key-name");
			Declaration namedDeclaration = mergedDeclarations.get(key);
			if (namedDeclaration == null)
				mergedDeclarations.put(key, bareDeclaration);
			else
				mergedDeclarations.put(
						key,
						mergeDeclarations(namedDeclaration, bareDeclaration)
				);
		}
		for (Declaration mergedDeclaration : mergedDeclarations.values())
			declarations.add(mergedDeclaration);
		return mergedDeclarations.values();
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
		Collection<Declaration> declarations = getUnmergedDeclarations(klass);
		if (translator != null) {
			for (Declaration declaration : declarations) {
				translateKey(declaration);
			}
		}
		return mergeDeclarationsByKey(declarations);
	}

	private void translateKey(Declaration declaration) {
		/*@Nullable*/ String name = declaration.getKey();
		if (name != null)
			name = translator.getKey(name);
		else if (declaration.getField() != null)
			name = translator.getKey(declaration.getField());
		else if (declaration.getGetter() != null)
			name = translator.getKey(Context.GETTER, declaration.getGetter());
		else if (declaration.getSetter() != null)
			name = translator.getKey(Context.SETTER, declaration.getSetter());
		else
			throw new InvalidDeclarationException("No annotatetd property specified");
		// Use BeanDec instead of GuardedDec to bypass value checks.
		((BeanDeclaration)declaration).setKey(name);
	}
}
