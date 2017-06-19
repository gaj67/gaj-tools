package gaj.analysis.bayes.pmf;

/**
 * Represents a {@link PMF} where the domain X = {x_1, x_2, ..., x_N} contains
 * distinct numerical values spread regularly across an interval, of the form
 * x_i = x_1 + (i - 1) h, where h = (x_N - x_1) / (N - 1).
 * 
 * @param <T>
 *            - The type of all values x in X.
 * @param <I>
 *            - The type of the number, N, of values in X.
 */
public interface IntervalPMF<T, I> extends PMF<T> {

    /**
     * Obtains the lowest numerical value x_1 in X.
     * 
     * @return The minimum x value.
     */
    T lower();

    /**
     * Obtains the highest numerical value x_N in X.
     * 
     * @return The maximum x value.
     */
    T upper();

    /**
     * Obtains the size, h, of the sub-interval between successive values, x_{i}
     * and x_{i+1}, in X.
     * 
     * @return
     */
    T step();

    /**
     * Obtains the number, N, of values in the domain X.
     * 
     * @return The number of values.
     */
    I size();

    /**
     * Obtains the value x_i = x_{j+1} in X for internal index j=0, 1, ..., N-1.
     * 
     * @param index
     *            - The index j.
     * @return The value x_i in X, or a value of -infinity if j < 0, or a value
     *         of +infinity if j > N-1.
     */
    T value(I index);

}
