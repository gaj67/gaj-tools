package gaj.analysis.bayes.pmf;

import gaj.analysis.bayes.ProbDist;

/**
 * Denotes a probability distribution, p(x), over a discrete domain, X, such
 * that 0 <= p(x) <= 1 and sum_{x in X} p(x) = 1.
 */
public interface PMF extends ProbDist {

}
