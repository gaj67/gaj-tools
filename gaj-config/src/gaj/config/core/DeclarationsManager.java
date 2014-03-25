package gaj.config.core;

import gaj.config.declaration.Declaration;
import gaj.config.declaration.InvalidDeclarationException;

import java.util.Map;

/**
 * Provides an interface for managing property declarations
 * on classes.
 */
public interface DeclarationsManager {

	Map<String,Declaration> getDeclarations(Class<?> klass);

	/**
	 * Constructs a new instance of the given class, configured with
	 * the given properties.
	 * @param klass - The class to construct.
	 * @param config - The instance properties.
	 * @return A new, configured instance, or a value of null
	 * if the instance cannot be constructed.
	 * @throws InvalidDeclarationException If a declared
	 *  property has a value but cannot be set.
	 *  @throws InvalidPropertiesException If a required property
	 *  does not have a value.
	 */
	<T> T newInstance(Class<T> klass, Properties config);

}