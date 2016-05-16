package gaj.config.configurators;

public interface Configurator {
 
	/**
	 * Configures the instance with the given properties.
	 * 
	 * @param instance - The instance to be configured.
	 * @param properties - The configuration properties. 
	 * @throws InvalidConfigurationException if the configuration fails. 
	 */
	<T> void configure(T instance, Configuration properties);

	/**
	 * Configures an instance of the given class with the given properties.
	 * 
	 * @param klass - The class to be configured.
	 * @param properties - The configuration properties.
	 * @return A configured instance of the class.
	 * @throws InvalidConfigurationException if the configuration fails. 
	 */
	<T> T configure(Class<T> klass, Configuration properties);

	default InvalidConfigurationException failure(String message) {
		return new InvalidConfigurationException(message);
	}

	default InvalidConfigurationException failure(Throwable t) {
		return new InvalidConfigurationException(t);
	}

}
