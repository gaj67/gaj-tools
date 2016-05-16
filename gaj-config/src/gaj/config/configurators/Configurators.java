package gaj.config.configurators;

import gaj.config.declaration.DeclarationManager;
import gaj.config.serialisers.Serialiser;

public abstract class Configurators {

	private Configurators() {}
	
	public static Configurator newConfigurator(DeclarationManager manager, Serialiser<?> serialiser) {
		return new ConfiguratorImpl(manager, serialiser);
	}
}
