package gaj.config.configurators;

public interface Configurator {
 
	/**
	 * Configures the instance with the given properties.
	 * 
	 * @param instance - The instance to be configured.
	 * @param properties - The configuration properties. 
	 * @param useGlobalNamespace - Indicates whether (true) or not (false) to use the class' global name-space
	 * when looking for configuration property values.
	 * @throws InvalidConfigurationException if the configuration fails. 
	 */
	<T> void configure(T instance, Configuration properties, boolean useGlobalNamespace);

	/**
	 * Configures an instance of the given class with the given properties.
	 * 
	 * @param klass - The class to be configured.
	 * @param properties - The configuration properties.
	 * @param useGlobalNamespace - Indicates whether (true) or not (false) to use the class' global name-space
	 * when looking for configuration property values.
	 * 
	 * @return A configured instance of the class.
	 * @throws InvalidConfigurationException if the configuration fails. 
	 */
	<T> T configure(Class<T> klass, Configuration properties, boolean useGlobalNamespace);

	default InvalidConfigurationException failure(String message) {
		return new InvalidConfigurationException(message);
	}

	default InvalidConfigurationException failure(Throwable t) {
		return new InvalidConfigurationException(t);
	}

}
