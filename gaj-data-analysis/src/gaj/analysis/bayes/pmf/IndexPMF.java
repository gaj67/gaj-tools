package gaj.analysis.bayes.pmf;

/**
 * Assumes that the domain X = {x_1, x_2, ..., x_N} consists of the index values
 * {start, start+1, ..., end-1, end}.
 * 
 * @param <T>
 *            - The type of all values x in X.
 */
public interface IndexPMF<T> extends PMF<T> {

    /**
     * Obtains the lowest index value x_1 in X.
     * 
     * @return The first index.
     */
    T start();

    /**
     * Obtains the highest (possibly infinite) index value x_N in X.
     * 
     * @return The last index.
     */
    T end();

    /**
     * Obtains the (possibly infinite) number N of index values in X.
     * 
     * @return The number of index values.
     */
    T size();

}
