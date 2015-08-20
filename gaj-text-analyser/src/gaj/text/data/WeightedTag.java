package gaj.text.data;

/**
 * Specifies a tag with a relative weight of importance.
 */
public interface WeightedTag extends Tag {

    /**
     * Obtains the tag weight, relative to other tags in the same conceptual group.
     *
     * @return The tag's weight.
     */
    double getWeight();

}
