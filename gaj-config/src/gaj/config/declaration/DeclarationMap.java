package gaj.config.declaration;


/**
 * Provides an interface for interrogating the merged
 * declarations of a class.
 */
public interface DeclarationMap {

	/**
	 * Obtains the number of distinct properties.
	 * 
	 * @return The number of properties.
	 */
	int numProperties();

	/**
	 * Obtains the key-names of all merged declarations.
	 * 
	 * @return An iterable over the key-names.
	 */
	Iterable<String> getKeys();

	/**
	 * Obtains the merged declarations.
	 * 
	 * @return An iterable over the declarations.
	 */
	Iterable<Declaration> getDeclarations();

	/**
	 * Obtains the declaration with the given key-name.
	 * 
	 * @param key - The key-name.
	 * @return The declaration, or a value of null
	 * if there is no declaration for that key.
	 */
	/*@Nullable*/ Declaration getDeclaration(String key);

}
