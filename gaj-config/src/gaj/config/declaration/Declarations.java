/*
 * (c) Geoff Jarrad, 2016.
 */
package gaj.config.declaration;

import gaj.config.annotations.Annotations;
import gaj.config.annotations.Default;
import gaj.config.annotations.Getter;
import gaj.config.annotations.Property;
import gaj.config.annotations.Required;
import gaj.config.annotations.Setter;
import gaj.config.keys.KeyTranslator;

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
	 * Extracts the untranslated property configuration declaration on a field.
	 * 
	 * @param field - The field to test.
	 * @return A declaration instance summarising the property annotations
	 * on the field, or the null value if there are no such annotations.
	 * @throws InvalidDeclarationException If the property annotations are inconsistent.
	 */
	public static /*@Nullable*/ Declaration getDeclaration(final Field field) {
		return getDeclaration(field, null);
	}

	/**
	 * Extracts the translated property configuration declaration on a field.
	 * 
	 * @param field - The field to test.
	 * @param keyTranslator - An optional translator for guessing the property key-name from
	 * the field name, if necessary.
	 * @return A declaration instance summarising the property annotations
	 * on the field, or the null value if there are no such annotations.
	 * @throws InvalidDeclarationException If the property annotations are inconsistent.
	 */
	public static /*@Nullable*/ Declaration getDeclaration(Field field, /*@Nullable*/ KeyTranslator translator)	{
		Required requiredAnno = field.getAnnotation(Required.class);
		Default defaultAnno = field.getAnnotation(Default.class);
		if (requiredAnno != null && defaultAnno != null)
			throw failure("Field " + field.getName() + " cannot both be required and have a default value");
		Property propertyAnno = field.getAnnotation(Property.class);
		if (propertyAnno == null && requiredAnno == null && defaultAnno == null)
			return null; // Not marked with any configuration annotations.
		BeanDeclaration dec = new BeanDeclaration();
		dec.setField(field);
		dec.setType(field.getType());
		if (propertyAnno != null)
			dec.setKey(propertyAnno.value());
		if (translator != null) {
			String key = dec.getKey();
			if (key == null)
				key = translator.guessPropertyKey(field);
			dec.setKey(translator.translateKey(key));
		}
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
	 * Extracts the untranslated property configuration declaration on a method.
	 * 
	 * @param method - The method to test.
	 * @return A declaration instance summarising the property annotations
	 * on the method, or the null value if there are no such annotations.
	 * @throws InvalidDeclarationException If the property annotations are inconsistent.
	 */
	public static /*@Nullable*/ Declaration getDeclaration(Method method) {
		return getDeclaration(method, null);
	}

	/**
	 * Extracts the translated property configuration declaration on a method.
	 * 
	 * @param method - The method to test.
	 * @param keyTranslator - An optional translator for guessing the property key-name from
	 * the method name, if necessary.
	 * @return A declaration instance summarising the property annotations
	 * on the method, or the null value if there are no such annotations.
	 * @throws InvalidDeclarationException If the property annotations are inconsistent.
	 */
	public static /*@Nullable*/ Declaration getDeclaration(Method method, /*@Nullable*/ KeyTranslator translator) {
		Getter getterAnno = method.getAnnotation(Getter.class);
		Setter setterAnno = method.getAnnotation(Setter.class);
		if (getterAnno != null && setterAnno != null)
			throw failure("Method " + method.getName() + "cannot be both a getter and setter");
		Required requiredAnno = method.getAnnotation(Required.class);
		Default defaultAnno = method.getAnnotation(Default.class);
		if (getterAnno == null && setterAnno == null && requiredAnno == null && defaultAnno == null)
			return null; // Not marked with any configuration annotations.
		Class<?>[] args = method.getParameterTypes();
		Class<?> retType = method.getReturnType();
		if (getterAnno != null) {
			if (args.length != 0)
				throw failure("Getter method " + method.getName() + " must not have arguments");
			if (retType == void.class)
				throw failure("Getter method " + method.getName() + " must have a non-void return type");
			return processGetter(method, retType, translator, getterAnno, requiredAnno,	defaultAnno);
		} else if (setterAnno != null) {
			if (args.length != 1)
				throw failure("Setter method " + method.getName() + " must have one argument");
			return processSetter(method, args[0], translator, setterAnno, requiredAnno,	defaultAnno);
		} else {
			// Guess getter or setter.
			if (args.length == 0 && retType != void.class)
				return processGetter(method, retType, translator, null, requiredAnno, defaultAnno);
			else if (args.length == 1)
				return processSetter(method, args[0], translator, null, requiredAnno, defaultAnno);
			else
				throw failure("Method " + method.getName() + " is not a getter or a setter");
		}
	}

	private static Declaration processSetter(Method method, Class<?> dataType, /*@Nullable*/ KeyTranslator translator, 
			/*@Nullable*/ Setter setterAnno,  /*@Nullable*/ Required requiredAnno, /*@Nullable*/ Default defaultAnno) 
	{
		if (requiredAnno != null && defaultAnno != null)
			throw failure("Setter method " + method.getName() + " cannot both be required and have a default value");
		BeanDeclaration dec = new BeanDeclaration();
		if (requiredAnno != null)
			dec.setRequired(true);
		else if (defaultAnno != null)
			dec.setDefault(defaultAnno.value());
		dec.setType(dataType);
		dec.setSetter(method);
		if (setterAnno != null)	dec.setKey(setterAnno.value());
		if (translator != null) {
			String key = dec.getKey();
			if (key == null) key = translator.guessSetterKey(method);
			dec.setKey(translator.translateKey(key));
		}
		return dec;
	}

	private static Declaration processGetter(Method method, Class<?> dataType, /*@Nullable*/ KeyTranslator translator,
			/*@Nullable*/ Getter getterAnno, /*@Nullable*/ Required requiredAnno, /*@Nullable*/ Default defaultAnno) 
	{
		if (requiredAnno != null)
			throw failure("Getter method " + method.getName() + " cannot be required");
		if (defaultAnno != null)
			throw failure("Getter method " + method.getName() + " cannot have a default value");
		BeanDeclaration dec = new BeanDeclaration();
		dec.setType(dataType);
		dec.setGetter(method);
		if (getterAnno != null) dec.setKey(getterAnno.value());
		if (translator != null) {
			String key = dec.getKey();
			if (key == null)
				key = translator.guessGetterKey(method);
			dec.setKey(translator.translateKey(key));
		}
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
	 * Obtains a collection of untranslated, unmerged property declarations from the given class. 
	 * The collection will be empty if the class is not configurable or has no declared properties.
	 * 
	 * @param klass - An allegedly configurable class, supposedly containing property declarations.
	 * @return A collection of property declarations.
	 * @throws InvalidDeclarationException If any property
	 * is marked with inconsistent settings.
	 */
	public static Collection<Declaration> getDeclarations(Class<?> klass) {
		return getDeclarations(klass, null);
	}

	/**
	 * Obtains a collection of translated but unmerged property declarations from the given class. 
	 * The collection will be empty if the class is not configurable or has no declared properties.
	 * 
	 * @param klass - An allegedly configurable class supposedly containing property declarations.
	 * @param translator - An optional translator for guessing property key-names if necessary.
	 * @return A collection of property declarations.
	 * @throws InvalidDeclarationException If any property
	 * is marked with inconsistent settings.
	 */
	public static Collection<Declaration> getDeclarations(Class<?> klass, /*@Nullable*/ KeyTranslator translator) {
		List<Declaration> declarations = new ArrayList<Declaration>();
		if (Annotations.isConfigurable(klass)) {
			for (Field field : klass.getFields()) {
				Declaration dec = getDeclaration(field, translator);
				if (dec != null)
					declarations.add(dec);
			}
			for (Method method : klass.getMethods()) {
				Declaration dec = getDeclaration(method, translator);
				if (dec != null)
					declarations.add(dec);
			}
		}
		return declarations;
	}

	/**
	 * Merges multiple property declarations with
	 * the same key-name string into a single declaration.
	 * 
	 * @param declarations - An iterable over property declarations.
	 * @return A map from key-names to merged property declarations.
	 * @throws InvalidDeclarationException If any property
	 * does not have a key-name, or is otherwise marked with
	 * inconsistent settings.
	 */
	public static DeclarationMap mergeDeclarationsByKey(Iterable<Declaration> declarations) {
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
		return new DeclarationMapImpl(mergedDeclarations);
	}

}
