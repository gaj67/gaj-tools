package gaj.analysis.bayes.pmf.impl;

import gaj.analysis.bayes.pmf.UnivariatePMF;
import gaj.common.annotations.PackagePrivate;

/**
 * Provides a standard Poisson distribution.
 */
@PackagePrivate class PoissonPMFImpl implements UnivariatePMF {

    private final double lambda;
    private final double prob0;
    private final double logLambda;

    /**
     * Initialises the Poisson distribution.
     * 
     * @param lambda
     *            - The Poisson event arrival rate.
     */
    @PackagePrivate PoissonPMFImpl(double lambda) {
        this.lambda = lambda;
        prob0 = Math.exp(-lambda);
        logLambda = Math.log(lambda);
    }
    
    @Override
    public int start() {
        return 0;
    }

    @Override
    public int end() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int size() {
        return Integer.MAX_VALUE;
    }

    @Override
    public double prob(int index) {
        if (index < 0) return 0.0;
        if (index == 0) return prob0;
        if (lambda > 300) {
            double logProb = -lambda;
            for (int i = 1; i < index; i++) {
                logProb += logLambda - Math.log(i);
            }
            return Math.exp(logProb);
        } else {
            double prob = prob0;
            for (int i = 1; i < index; i++) {
                prob *= lambda / i;
            }
            return prob;
        }
    }

}
