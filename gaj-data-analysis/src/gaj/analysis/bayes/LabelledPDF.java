package gaj.analysis.bayes;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Labels each element of a PDF with a unique value.
 */
public interface LabelledPDF<X extends Comparable<?>> extends PDF {

    /**
     * Obtains the label of the given element.
     * 
     * @param index
     *            - The index of the element, starting from zero.
     * @return The label of the index, or a value of null if the index is out of
     *         range.
     */
    @Nullable X label(int index);

    /**
     * Obtains the index of the element corresponding to the given label.
     * 
     * @param label
     *            - The label.
     * @return The element index, or a value of -1 if the label does not
     *         correspond to any element.
     */
    int index(X label);

}
