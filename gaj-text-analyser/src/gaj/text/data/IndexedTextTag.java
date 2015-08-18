package gaj.text.data;

/**
 * Specifies a numbered textual annotation with a relative weight of importance.
 * This is useful if the tag is one of a closed set of alternatives.
 */
public interface IndexedTextTag extends TextTag {

    /**
     * Obtains the tag text index, relative to other tags.
     *
     * @return The tag's index.
     */
    int getIndex();

}
