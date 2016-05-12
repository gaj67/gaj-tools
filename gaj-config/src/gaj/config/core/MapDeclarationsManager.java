package gaj.config.core;

import gaj.config.declaration.Declaration;
import gaj.config.declaration.DeclarationFactory;
import gaj.config.declaration.InvalidDeclarationException;
import gaj.config.keys.KeyTranslator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapDeclarationsManager implements DeclarationsManager {

	private final SerialisationManager serialiser;
	private final KeyTranslator translator;
	private final Map<Class<?>,Map<String,Declaration>>
	classDeclarations = new HashMap<Class<?>,Map<String,Declaration>>();

	private MapDeclarationsManager(
			SerialisationManager serialiser,
			KeyTranslator translator)
	{
		this.serialiser = serialiser;
		this.translator = translator;
	}

	public Map<String,Declaration> getDeclarations(Class<?> klass)
	{
		Map<String, Declaration> declarations =
				classDeclarations.get(klass);
		if (declarations == null) {
			Collection<Declaration> _declarations =
					getPropertyDeclarations(klass);
			if (translator != null) {
				for (Declaration declaration : _declarations)
					DeclarationFactory.translatePropertyKey(declaration, translator);
			}
			declarations = DeclarationFactory.mergeDeclarationsByKey(_declarations);
			if (serialiser != null) {
				for (Declaration declaration : declarations.values())
					deserialiseDefaultValue(declaration, serialiser);
			}
			classDeclarations.put(klass, declarations);
		}
		return declarations;
	}

	public <T> T newInstance(Class<T> klass, Properties config) {
		try {
			T instance = klass.newInstance();
			Map<String,Declaration> declarations = getDeclarations(klass);
			Set<Declaration> toDo
			= new HashSet<Declaration>(declarations.values());
			for (String key : config.getPropertyKeys()) {
				Declaration declaration = declarations.get(key);
				if (declaration == null) continue; // Ignore.
				setProperty(instance, declaration, config.getProperty(key));
				toDo.remove(declaration);
			}
			for (Declaration declaration : toDo) {
				if (declaration.hasDefault())
					setProperty(instance, declaration, declaration.getValue());
				else if (declaration.isRequired())
					throw new InvalidPropertiesException("Missing value for required property: " + declaration);
			}
			return instance;
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
	}

	private void setProperty(Object instance, Declaration declaration, Object value) {
		try {
			Method setter = declaration.getSetter();
			if (setter != null) {
				setter.invoke(instance, value);
			} else {
				Field field = declaration.getField();
				if (field == null)
					throw new InvalidDeclarationException("Cannot set value for declaration: " + declaration);
				field.set(instance, value);
			}
		} catch (IllegalArgumentException e) {
			throw new InvalidPropertiesException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new InvalidDeclarationException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new InvalidDeclarationException(e.getMessage());
		}
	}

}