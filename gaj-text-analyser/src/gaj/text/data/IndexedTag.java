package gaj.text.data;

/**
 * Specifies a numbered textual annotation, out of a closed set of alternatives.
 */
public interface IndexedTag extends Tag {

    /**
     * Obtains the tag index, relative to other tags in the same conceptual group.
     *
     * @return The tag's index, commencing from zero.
     */
    int getIndex();

}
