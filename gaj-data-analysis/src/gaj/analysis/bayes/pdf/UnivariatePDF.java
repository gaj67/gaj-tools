package gaj.analysis.bayes.pdf;

/**
 * Represents a probability distribution on a univariate, continuous domain X,
 * with probability density function f:X -> [0, inf), such that int_X f(x) dx =
 * 1. For convenience, we assume X = [L, U].
 */
public interface UnivariatePDF extends PDF {

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
     * Obtains the probability density, f(x), at the specified x value.
     * 
     * @param x
     *            - The x value.
     * @return The density at x, or a value of zero if x < L or x > U.
     */
    double density(double x);

}
