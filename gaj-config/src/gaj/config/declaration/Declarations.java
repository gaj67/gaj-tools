/*
 * (c) Geoff Jarrad, 2016.
 */
package gaj.config.declaration;

import gaj.config.annotations.Default;
import gaj.config.annotations.Getter;
import gaj.config.annotations.Property;
import gaj.config.annotations.Required;
import gaj.config.annotations.Setter;

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
public abstract class Declarations {

	private Declarations() {}

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
			throw failure("Field " + field.getName() + " cannot both be required and have a default value");
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

	private static InvalidDeclarationException failure(String message) {
		return new InvalidDeclarationException(message);
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
			throw failure("Method " + method.getName() + "cannot be both a getter and setter");
		Required requiredAnno = method.getAnnotation(Required.class);
		Default defaultAnno = method.getAnnotation(Default.class);
		if (requiredAnno != null && defaultAnno != null)
			throw failure("Method " + method.getName() + " cannot both be required and have a default value");
		if (getterAnno == null && setterAnno == null && requiredAnno == null && defaultAnno == null)
			return null; // Not marked with any configuration annotations.
		GuardedDeclaration dec = new GuardedDeclaration();
		Class<?>[] args = method.getParameterTypes();
		if (getterAnno != null) {
			if (args == null || args.length != 0)
				throw failure("Method " + method.getName() + " is not a simple getter");
			Class<?> retType = method.getReturnType();
			if (retType == null || retType == void.class)
				throw failure("Method " + method.getName() + " is not a getter");
			dec.setType(retType);
			dec.setGetter(method);
			dec.setKey(getterAnno.value());
		} else if (setterAnno != null) {
			if (args == null || args.length != 1)
				throw failure("Method " + method.getName() + " is not a simple setter");
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
	 * declarations <em>for the same property</em> in a consistent fashion.
	 * 
	 * @param declarations - An array of property declarations.
	 * @return The combined property declaration.
	 * @throws InvalidDeclarationException If the given property
	 * declarations have incompatible settings, or if no property
	 * declarations are given.
	 */
	public static Declaration mergePropertyDeclarations(Declaration... declarations) {
		if (declarations.length == 0)
			throw failure("No property declarations have been given");
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
	public static Collection<Declaration> getUnmergedDeclarations(Class<?> klass) {
		List<Declaration> declarations = new ArrayList<Declaration>();
		for (Field field : klass.getFields()) {
			Declaration dec = getDeclaration(field);
			if (dec != null)
				declarations.add(dec);
		}
		for (Method method : klass.getMethods()) {
			Declaration dec = getDeclaration(method);
			if (dec != null)
				declarations.add(dec);
		}
		return declarations;
	}

	/**
	 * Merges multiple property declarations with
	 * the same key-name string into a single declaration.
	 * 
	 * @param declarations - An iterable over property declarations.
	 * @return A collection of merged property declarations.
	 * @throws InvalidDeclarationException If any property
	 * does not have a key-name, or is otherwise marked with
	 * inconsistent settings.
	 */
	public static Collection<Declaration> mergeDeclarationsByKey(Iterable<Declaration> declarations) {
		Map<String,Declaration> mergedDeclarations = new HashMap<>();
		for (Declaration bareDeclaration : declarations) {
			String key = bareDeclaration.getKey();
			if (key == null)
				throw failure("Cannot merge property declarations with a null key-name");
			Declaration namedDeclaration = mergedDeclarations.get(key);
			namedDeclaration = (namedDeclaration == null) 
					? bareDeclaration
							: mergePropertyDeclarations(namedDeclaration, bareDeclaration);
			mergedDeclarations.put(key, namedDeclaration);
		}
		return mergedDeclarations.values();
	}

}
