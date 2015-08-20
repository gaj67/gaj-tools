package gaj.text.data;

/**
 * Represents a dictionary of tokens, each of which is described by one or more tags.
 */
public interface Dictionary<T extends Tag> {

    /**
     * Obtains the definition of a token.
     * 
     * @param token - The token text.
     * @return The token definition, or a value of null if the dictionary does not contain the token.
     */
    /*@Nullable*/ Definition<T> getDefinition(String token);

}
