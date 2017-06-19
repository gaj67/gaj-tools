package gaj.analysis.bayes.pmf.impl.integer;

import gaj.common.annotations.PackagePrivate;

/**
 * Provides a Poisson distribution conditional on the fact that a given minimum
 * number of counts is always observed.
 */
@PackagePrivate class LeftTruncatedPoissonPMFImpl extends PoissonPMFImpl {

    private final int minCount;
    private final double normProb;

    /**
     * Initialises the left-truncated Poisson distribution, P(N=n | N>=c,
     * lambda).
     * 
     * @param lambda
     *            - The Poisson event arrival rate.
     * @param minCount
     *            - The minimum observed count, c.
     */
    @PackagePrivate LeftTruncatedPoissonPMFImpl(double lambda, int minCount) {
        super(lambda);
        this.minCount = minCount;
        // Compute p(N < c | lambda>)
        double prob = (minCount <= 0) ? 1 : 0;
        for (int i = 0; i < minCount; i++)
            prob += super.prob(i);
        this.normProb = 1 / (1 - prob);
    }

    @Override
    public Integer start() {
        return minCount;
    }

    @Override
    public double prob(Integer index) {
        if (index < minCount) return 0;
        return super.prob(index) * normProb;
    }

}
