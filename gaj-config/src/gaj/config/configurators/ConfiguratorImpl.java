package gaj.config.configurators;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import gaj.config.declaration.Declaration;
import gaj.config.declaration.DeclarationManager;
import gaj.config.declaration.DeclarationMap;
import gaj.config.declaration.InvalidDeclarationException;
import gaj.config.serialisers.InvalidSerialisationException;
import gaj.config.serialisers.Serialiser;

/*package-private*/ class ConfiguratorImpl implements Configurator {

	private final DeclarationManager manager;
	private final Serialiser<? extends Object> serialiser;

	/*package-private*/ ConfiguratorImpl(DeclarationManager manager, Serialiser<?> serialiser) {
		this.manager = manager;
		this.serialiser = serialiser;
	}

	@Override
	public <T> void configure(T instance, Configuration properties, boolean useGlobalNamespace) {
		try {
			DeclarationMap declarations = manager.getDeclarationMap(instance.getClass(), useGlobalNamespace);
			configure(instance, declarations, properties);
		} catch (InvalidDeclarationException | InvalidSerialisationException e) {
			throw failure(e);
		}
	}

	@Override
	public <T> T configure(Class<T> klass, Configuration properties, boolean useGlobalNamespace) {
		try {
			T instance = klass.newInstance();
			configure(instance, properties, useGlobalNamespace);
			return instance;
		} catch (InstantiationException | IllegalAccessException e) {
			throw failure(e);
		}
	}

	private void configure(Object instance, DeclarationMap declarations, Configuration properties) {
		for (Declaration declaration : declarations.getDeclarations()) {
			if (!declaration.isSettable()) continue;
			String value = properties.getValue(declaration.getKey());
			if (value != null)
				setValue(instance, declaration, serialiser.deserialise(value));
			else if (declaration.hasDefault())
				setValue(instance, declaration, serialiser.deserialise(declaration.getValue()));
			else if (declaration.isRequired())
				throw failure("Missing value for required property " + declaration.getKey());
		}
	}

	private void setValue(Object instance, Declaration declaration, Object value) {
		try {
			Method setter = declaration.getSetter();
			if (setter != null) {
				setter.invoke(instance, value);
			} else {
				Field field = declaration.getField();
				if (field == null)
					throw failure("Cannot set value for property " + declaration.getKey());
				field.set(instance, value);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw failure(e);
		}
	}

}
