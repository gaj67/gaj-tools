package gaj.text.data;

/**
 * Specifies a textual annotation with a relative weight of importance.
 */
public interface TextTag {

    /**
     * Obtains the tag text.
     *
     * @return The tag's text.
     */
    String getText();

    /**
     * Obtains the tag weight, relative to other tags.
     *
     * @return The tag's weight.
     */
    double getWeight();

}
