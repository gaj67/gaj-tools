package gaj.analysis.model.score;

/**
 * Indicates a data case with an associated discrete label, such as a class or
 * category.
 *
 * <I> - The type of data.
 */
public interface LabelledDatum<I> extends Datum<I> {

    /**
     * Optionally obtains an index into an ordered list of discrete labels.
     * 
     * @return The index of the case label, or a negative value to indicate that
     *         the label is unknown.
     */
    int getLabelIndex();

}
