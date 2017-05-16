package gaj.analysis.bayes.pmf.impl;

import gaj.common.annotations.PackagePrivate;

/**
 * Provides a zero-inflated Poisson distribution, where the usual Poisson
 * process might be randomly switched off at any given time. Hence, the
 * probability of observing zero events is higher than for the usual Poisson
 * process.
 */
@PackagePrivate class ZeroInflatedPoissonPMFImpl extends PoissonPMFImpl {

    private final double alpha;
    private final double oneMinusAlpha;

    /**
     * Initialises the zero-inflated Poisson distribution.
     * 
     * @param lambda
     *            - The Poisson event arrival rate.
     * @param alpha
     *            - The probability that the Poisson process has been switched
     *            off.
     */
    @PackagePrivate ZeroInflatedPoissonPMFImpl(double lambda, double alpha) {
        super(lambda);
        this.alpha = alpha;
        this.oneMinusAlpha = 1 - alpha;
    }

    @Override
    public double prob(int index) {
        return (index < 0) ? 0 : (index == 0) ? (alpha + oneMinusAlpha * super.prob(0)) : (oneMinusAlpha * super.prob(index));
    }

}
