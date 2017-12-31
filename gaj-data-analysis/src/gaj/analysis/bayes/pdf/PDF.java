package gaj.analysis.bayes.pdf;

import gaj.analysis.bayes.ProbDist;

/**
 * Represents a probability distribution on a continuous domain X, with
 * probability density function f:X -> [0, inf), such that int_X f(x) dx = 1.
 */
public interface PDF extends ProbDist {

}
