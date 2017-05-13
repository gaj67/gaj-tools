package gaj.analysis.bayes.pdf;

import gaj.analysis.bayes.ProbDist;

/**
 * Denotes a probability distribution, p(x), over a continuous domain, X, such
 * that p(x) >= 0 and int_{X} p(x) dx = 1.
 */
public interface PDF extends ProbDist {

}
