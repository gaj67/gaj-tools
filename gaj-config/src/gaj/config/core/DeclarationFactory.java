package gaj.config.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.DeclarationFactory.KeyNameTranslator.NameContext;
import config.PropertiesFactory.InvalidPropertiesException;
import config.PropertiesFactory.Properties;
import gaj.config.core.DeclarationsManager;
import gaj.config.core.MapDeclarationsManager;
import gaj.config.declaration.KeyTranslator;
import gaj.config.serial.InvalidSerialisationException;
import gaj.config.serial.MultiSerialiser;
import gaj.config.serial.Serialiser;

/**
 * This module deals with the declaration of configurable properties for a class.
 */
public class DeclarationFactory {

	/**
	 * Determines if a class is configurable.
	 * @param klass - The class to test.
	 * @return A value of true (or false) if the class is (or is not)
	 * marked as being configurable.
	 */
	public static boolean isConfigurable(Class<?> klass) {
		return klass.isAnnotationPresent(Configurable.class);
	}

	/**
	 * Determines if the class should be instantiated as a singleton.
	 * @param klass - The class to test.
	 * @return A value of true (or false) if the class is configurable and
	 * is (or is not) to be instantiated as a singleton,
	 * or null if the class is not configurable.
	 */
	public static boolean isSingleton(Class<?> klass) {
		return klass.isAnnotationPresent(Singleton.class);
	}

	/**
	 * Determines if a field represents a property.
	 * @param  - The field to test.
	 * @return A value of true (or false) if the field is (or is not)
	 * marked as being a property.
	 */
	public static boolean isProperty(Field field) {
		return field.isAnnotationPresent(Property.class);
	}

	/**
	 * Obtains the optional, user-specified key-name of the property.
	 * 
	 * @param  - The field to test.
	 * @return The string key-name of the property if it is specified,
	 * or a null value if it is not specified or
	 * if the field is not a property.
	 */
	public static /*@Nullable*/ String getPropertyKey(Field field) {
		Property anno = field.getAnnotation(Property.class);
		if (anno == null) return null;
		String key = anno.value();
		return (key == Property.DEFAULT_KEY) ? null : key;
	}

	/**
	 * Determines if a method represents a simple property getter.
	 * 
	 * @param  - The method to test.
	 * @return A value of true (or false) if the method is (or is not)
	 * marked as being a property getter, has no arguments,
	 * and has a non-void return type.
	 */
	public static boolean isGetter(Method method) {
		return method.isAnnotationPresent(Getter.class)
				&& method.getReturnType() != Void.class 
				&& method.getParameterTypes().length == 0;
	}

	/**
	 * Determines if a method represents a simple property setter.
	 * 
	 * @param  - The method to test.
	 * @return A value of true (or false) if the method is (or is not)
	 * marked as being a property setter, and has exactly one argument.
	 */
	public static boolean isSetter(Method method) {
		return method.isAnnotationPresent(Setter.class)
				&& method.getParameterTypes().length == 1;
	}

	/**
	 * Obtains the optional key-name of the property
	 * associated with the getter or setter method.
     *
	 * @param  - The method to test.
	 * @return The string key-name of the property if it is specified,
	 * or a null value if it is not specified or
	 * if the method is not a property getter or setter.
	 */
	public static String getPropertyKey(Method method) {
		Annotation anno = method.getAnnotation(Getter.class);
		if (anno != null) {
			String key = ((Getter)anno).value();
			return (Property.DEFAULT_KEY == key) ? null : key;
		}
		anno = method.getAnnotation(Setter.class);
		if (anno == null) return null;
		String key = ((Setter)anno).value();
		return (Property.DEFAULT_KEY == key) ? null : key;
	}

	/**
	 * Determines if a field is a required property. 
	 * By default, a property value is optional. 
	 * If required, then a value must
	 * be specified by external configuration. 
	 * 
	 * @param  - The field to test.
	 * @return A value of true (or false) if the field is (or is not)
	 * marked as being a required property.
	 */
	public static boolean isRequired(Field field) {
		return field.isAnnotationPresent(Required.class);
	}

	/**
	 * Determines if a method has a required property. 
	 * By default, a property value is optional. 
	 * If required, then a value must
	 * be specified by external configuration. 
	 * 
	 * @param  - The method to test.
	 * @return A value of true (or false) if the method is (or is not)
	 * marked as having a required property.
	 */
	public static boolean isRequired(Method method) {
		return method.isAnnotationPresent(Required.class);
	}

	/**
	 * Determines if a field has a default property value.
	 * 
	 * @param  - The field to test.
	 * @return A value of true (or false) if the field is (or is not)
	 * marked as having a default property value.
	 */
	public static boolean hasDefault(Field field) {
		return field.isAnnotationPresent(Default.class);
	}

	/**
	 * Obtains the default serialised value of the property
	 * associated with the given field.
	 * 
	 * @param  - The field to test.
	 * @return The string representation of the default value
	 * of the associated property, or a null value if the default
	 * is not specified.
	 */
	public static /*@Nullable*/ String getDefaultValue(Field field) {
		Default anno = field.getAnnotation(Default.class);
		return (anno == null) ? null : anno.value();
	}

	/**
	 * Determines if a method has a default property value.
	 * 
	 * @param  - The method to test.
	 * @return A value of true (or false) if the method is (or is not)
	 * marked as having a default property value.
	 */
	public static boolean hasDefault(Method method) {
		return method.isAnnotationPresent(Default.class);
	}

	/**
	 * Obtains the optional default value of the property
	 * associated with the given method.
     *
	 * @param  - The method to test.
	 * @return The string representation of the default value
	 * of the associated property, or a null value if the default
	 * is not specified.
	 */
	public static /*@Nullable*/ String getDefaultValue(Method method) {
		Default anno = method.getAnnotation(Default.class);
		return (anno == null) ? null : anno.value();
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
	 * Obtains a collection of raw (unmrged) 
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
	public static Collection<Declaration>
	getDeclarations(Class<?> klass)
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
	 * @return A collections of merged property declarations.
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
	 * Attempts to deserialise a default property value that
	 * has been constrained by the @Default annotation to be
	 * specified as a string.
	 * The value is only deserialised if
	 * it exists, is not null, is a string, and the target property
	 * type is not a string. Any other potential inconsistencies
	 * between value and property types are not checked.
	 * @param declaration - A property declaration.
	 * @param serialiser - A serialisation manager that can
	 * determine the correct type of serialiser for each property type.
	 * @throws InvalidSerialisationException If a serialiser cannot be
	 * found for a property type, or the deserialisation process fails.
	 */
	public static void deserialiseDefaultValue(
			Declaration declaration,
			SerialisationManager serialiser)
					throws InvalidSerialisationException
					{
		if (!declaration.hasDefault() || declaration.getType() == String.class)
			return;
		Object value = declaration.getValue();
		if (!(value instanceof String)) return; // Already deserialised?
		Serialiser deser = serialiser.getSerialiser(declaration.getType());
		if (deser == null)
			throw new InvalidSerialisationException("No deserialiser found for type: "+declaration.getType());
		value  = deser.deserialise((String)value);
		// Use BeanPD instead of GuardedPD to bypass value checks.
		if (!(declaration instanceof BeanDeclaration))
			throw new InvalidDeclarationException("Cannot deserialise declaration: "+declaration);
		((BeanDeclaration)declaration).setDefault(value);
					}

	/**
	 * Translates the key-name of the given property.
	 * The name to be translated is provided from the declaration,
	 * if specified, or from the name of the annotated field or method.
	 * @param declarations - A collection of property declarations.
	 * @param translator - An instance of a KeyNameTranslator.
	 */
	public static void translatePropertyKey(
			Declaration declaration,
			KeyTranslator translator)
	{
		NameContext context = null;
		String name = declaration.getKey();
		if (name != null) {
			context = NameContext.GIVEN;
		} else if (declaration.getField() != null) {
			context = NameContext.FIELD;
			name = declaration.getField().getName();
		} else if (declaration.getGetter() != null) {
			context = NameContext.GETTER;
			name = declaration.getGetter().getName();
		} else if (declaration.getSetter() != null) {
			context = NameContext.SETTER;
			name = declaration.getSetter().getName();
		} else {
			throw new InvalidDeclarationException("No annotatetd property specified");
		}
		name = translator.getKey(context, name);
		// Use BeanPD instead of GuardedPD to bypass value checks.
		((BeanDeclaration)declaration).setKey(name);
	}

	public static DeclarationsManager newDeclarationsManager(
			SerialisationManager serialiser,
			KeyTranslator translator)
	{
		return new MapDeclarationsManager(serialiser, translator);
	}

	public static DeclarationsManager newDeclarationsManager() {
		return new MapDeclarationsManager(null, null);
	}

}
