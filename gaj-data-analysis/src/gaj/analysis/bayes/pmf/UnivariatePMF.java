package gaj.analysis.bayes.pmf;

/**
 * Represents a probability distribution on a univariate, discrete domain X,
 * with a probability mass function, f:X -> [0, 1], such that sum_{x in X} f(x)
 * = 1. 
 * <br/> The domain X is countable, so we let X = {x_i | i in {S, S+1, ..., E-1, E}}.
 */
public interface UnivariatePMF extends PMF {

    /**
     * Obtains the index S of the first element in the countable domain X.
     * 
     * @return The first index.
     */
    int start();

    /**
     * Obtains the (possibly infinite) index E of the last element in the
     * countable domain X.
     * 
     * @return The last index.
     */
    int end();

    /**
     * Obtains the (possibly infinite) number N of elements in the discrete
     * domain X.
     * 
     * @return The number of elements.
     */
    int size();

    /**
     * Obtains the probability mass, f(x_i), of the specified element, x_i.
     * 
     * @param index
     *            - The index, i of the element x_i.
     * @return The probability value, f(x_i), for S <= i <= E, or a value of
     *         zero if i < S or i > E.
     */
    double prob(int index);

}
