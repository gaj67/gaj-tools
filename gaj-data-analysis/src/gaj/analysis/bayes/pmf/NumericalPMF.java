package gaj.analysis.bayes.pmf;

/**
 * Represents a {@link UnivariatePMF} where the domain X = {x_i | i in {S, S+1, ..., E-1, E}} 
 * contains distinct numerical elements x_i in [L, U].
 */
public interface NumericalPMF extends UnivariatePMF {

    /**
     * Obtains the lower bound L of the domain [L, U].
     * 
     * @return The minimum x value.
     */
    double lower();

    /**
     * Obtains the upper bound U of the domain [L, U].
     * 
     * @return The maximum x value.
     */
    double upper();

    /**
     * Obtains the value x_i for index i.
     * 
     * @param index
     *            - The index i.
     * @return The value L <= x_i <= U for S <= i <= E, or a value of -infinity
     *         if i < S, or a value of +infinity if i > E.
     */
    double value(int index);

    /**
     * Obtains the index i of the element x_i nearest to the given value x.
     * 
     * @param value
     *            - The value x.
     * @return The index S <= i <= E if L <= x <= U, or a value of -infinity if
     *         x < L, or a value of +infinity if x > U.
     */
    int index(double value);

}
