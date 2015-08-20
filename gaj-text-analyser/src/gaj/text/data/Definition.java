package gaj.text.data;

import java.util.List;

/**
 * Represents the definition of a token, as described by one or more tags.
 */
public interface Definition<T extends Tag> {

    /**
     * Obtains the token text.
     *
     * @return The definition token.
     */
    String getText();

    /**
     * Obtains the tags describing the token.
     *
     * @param token - The token text.
     * @return A (possibly empty) list of tags.
     */
    List<T> getTags();

}
