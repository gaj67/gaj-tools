package gaj.analysis.bayes.pmf;

import gaj.analysis.bayes.ProbDist;

/**
 * Denotes a probability distribution, p(x), over a countable domain, X = {x_1,
 * x_2, ..., x_N}, with probability mass function f:X -> [0, 1], such that
 * sum_{x in X} f(x) = 1.
 * 
 * @param <T>
 *            - The type of all values x in X.
 */
public interface PMF<T> extends ProbDist {

    /**
     * Obtains the probability mass, f(x), of the specified value, x.
     * 
     * @param value
     *            - The value, x.
     * @return The probability of x if x in X, or a value of zero otherwise.
     */
    double prob(T value);

}
