package gaj.analysis.bayes.pmf;

/**
 * Represents a finite and modifiable probability mass function (see
 * {@link PMF}).
 * 
 * @param <T>
 *            - The type of all values x in X.
 */
public interface EmpiricalPMF<T> extends PMF<T> {

    /**
     * Sets the probability mass, p(x), for the specified value x in X. This
     * method has no effect if x not in X. The resulting value may be
     * unnormalised.
     * 
     * 
     * @param value
     *            - The value, x.
     * @param prob
     *            - The probability value, f(x_i).
     */
    void set(T value, double prob);

    /**
     * Increments the probability mass, p(x), for the specified value x in X.
     * This method has no effect if x not in X. The resulting value may be
     * unnormalised.
     * 
     * 
     * @param value
     *            - The value, x.
     * @param probInc
     *            - The incremental probability value.
     */
    void add(T value, double probInc);

    /**
     * Scales the probability mass, p(x), for the specified value x in X. This
     * method has no effect if x not in X. The resulting value may be
     * unnormalised.
     * 
     * 
     * @param value
     *            - The value, x.
     * @param probScale
     *            - The probability scaling value.
     */
    void mult(T value, double probScale);

    /**
     * Normalises the probability mass values across all elements, such that
     * sum_{x in X} p(x) = 1.
     * 
     * @throws IllegalArgumentException
     *             if any p(x) < 0.
     */
    void normalise();

}
