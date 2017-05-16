package gaj.analysis.bayes.pmf;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Each element of the discrete domain X = {x_i | i in {S, S+1, ..., E-1, E}} is
 * a unique instance of the specified type, T.
 */
public interface LabelledPMF<T> extends UnivariatePMF {

    /**
     * Obtains the value, x_i, of the given element with index i.
     * 
     * @param index
     *            - The index i of the element.
     * @return The value x_i of the element for S <= i <= E, or a value of null
     *         if i < S or i > E.
     */
    @Nullable
    T value(int index);

    /**
     * Obtains the index, i, of the element corresponding to the given value,
     * x_i.
     * 
     * @param value
     *            - The value, x_i.
     * @return The element index, i in {S, S+1, ..., E-1, E}, or a value of
     *         -infinity if the value does not correspond to any element.
     */
    int index(T value);

}
