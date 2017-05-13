package gaj.analysis.bayes.pmf;

/**
 * Represents a probability distribution on a univariate, discrete domain X,
 * with a probability mass function, f:X -> [0, 1], such that sum_{x in X} f(x)
 * = 1. 
 * <br/> The domain X is countable, so we let I = {start, start+1, ..., end-1,
 * end} and X = {x_i | i in I}.
 */
public interface UnivariatePMF extends PMF {

    /**
     * Obtains the index of the first element in the countable domain X.
     * 
     * @return The first index.
     */
    int start();

    /**
     * Obtains the (possibly infinite) index of the last element in the
     * countable domain X.
     * 
     * @return The last index.
     */
    int end();

    /**
     * Obtains the (possibly infinite) number of elements in the discrete domain
     * X.
     * 
     * @return The number of elements.
     */
    int size();

    /**
     * Obtains the probability mass, f(x_i), of the specified element, x_i.
     * 
     * @param index
     *            - The index, i of the element x_i.
     * @return The probability value, f(x_i), for start <= i <= end, or a value
     *         of zero if i < start or i > end.
     */
    double prob(int index);

}
