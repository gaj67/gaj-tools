package gaj.config.configurators;

import gaj.config.declaration.Declaration;
import gaj.config.declaration.DeclarationMap;
import gaj.config.serialisers.InvalidSerialisationException;
import gaj.config.serialisers.Serialiser;

public abstract class Configurators {

	private Configurators() {}
	
	public static void configure(Object instance, DeclarationMap declarations, PropertyMap properties, Serialiser<? extends Object> serialiser) {
		for (Declaration declaration : declarations.getDeclarations()) {
			if (!declaration.isSettable()) continue;
			String value = properties.getValue(declaration.getKey());
			if (value != null)
				setValue(instance, declaration, deserialise(serialiser, value));
			else if (declaration.hasDefault())
				setValue(instance, declaration, deserialise(serialiser, declaration.getValue()));
			else if (declaration.isRequired())
				throw failure("Missing value for required property " + declaration.getKey());
		}
	}

	private static Object deserialise(Serialiser<? extends Object> serialiser, String value) {
		try {
			return serialiser.deserialise(value);
		} catch (InvalidSerialisationException e) {
			throw failure(e);
		}
	}

	private static InvalidConfigurationException failure(String message) {
		return new InvalidConfigurationException(message);
	}

	private static InvalidConfigurationException failure(Throwable t) {
		return new InvalidConfigurationException(t);
	}

	public static void setValue(Object instance, Declaration declaration, Object value) {
		// TODO Auto-generated method stub
		
	}

}
