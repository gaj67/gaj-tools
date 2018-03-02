package gaj.analysis.model.score;

import gaj.analysis.data.DataObject;

/**
 * Indicates a data case with an associated discrete label, such as a class or
 * category.
 *
 * <I> - The type of data.
 */
public interface LabelledDataCase<I extends DataObject> extends DataCase<I> {

    /**
     * Optionally obtains an index into an ordered list of discrete labels.
     * 
     * @return The index of the case label, or a negative value to indicate that
     *         the label is unknown.
     */
    int getLabelIndex();

}
