package gaj.analysis.bayes.pmf;

import gaj.analysis.bayes.ProbDist;

/**
 * Denotes a discrete probability distribution, p(x), over a finitely or
 * infinitely countable domain, X = {x_1, x_2, ..., x_N}, such that 0 <= p(x) <=
 * 1 and sum_{x in X} p(x) = 1.
 * 
 * @param <T>
 *            - The type of all values x in X.
 */
public interface PMF<T> extends ProbDist {

    /**
     * Obtains the probability mass, 0 <= p(x) <= 1, of the specified value, x.
     * 
     * @param value
     *            - The value, x.
     * @return The probability of x if x in X, or a value of zero otherwise.
     */
    double prob(T value);

}
