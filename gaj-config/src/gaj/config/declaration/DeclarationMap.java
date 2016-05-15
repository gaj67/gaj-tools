package gaj.config.declaration;


/**
 * Provides an interface for interrogating the merged
 * declarations of a class.
 */
public interface DeclarationMap {

	/**
	 * Obtains the number of merged property keys.
	 * 
	 * @return The number of merged declarations.
	 */
	int numKeys();

	/**
	 * Obtains the key-names of all merged declarations.
	 * 
	 * @return An iterable key-names.
	 */
	Iterable<String> getKeys();

	/**
	 * Obtains the declaration with the given key-name.
	 * 
	 * @param key - The key-name.
	 * @return The declaration, or a value of null
	 * if there is no declaration for that key.
	 */
	/*@Nullable*/ Declaration getDeclaration(String key);

}
