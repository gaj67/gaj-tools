package gaj.analysis.bayes.pmf.impl;

import gaj.analysis.bayes.pmf.NumericalPMF;
import gaj.common.annotations.PackagePrivate;

@PackagePrivate class RegularNumericalPMFImpl extends EmpiricalPMFImpl implements NumericalPMF {

    private final double lower;
    private final double upper;
    private final double step;

    @PackagePrivate RegularNumericalPMFImpl(int startIndex, int numElements, double lower, double upper) {
        super(startIndex, numElements);
        if (numElements < 2)
            throw new IllegalArgumentException("Must specify at least two elements");
        this.lower = lower;
        this.step = (upper - lower) / (numElements - 1);
        // this.upper = upper;
        this.upper = lower + step * (numElements - 1);
    }

    @Override
    public double lower() {
        return lower;
    }

    @Override
    public double upper() {
        return upper;
    }

    @Override
    public double value(int index) {
        if (index < start()) return Double.NEGATIVE_INFINITY;
        if (index > end()) return Double.POSITIVE_INFINITY;
        return lower + step * (index - start());
    }

    @Override
    public int index(double value) {
        if (value < lower) return Integer.MIN_VALUE;
        if (value > upper) return Integer.MAX_VALUE;
        return (int) (Math.round((value - lower) / step) + start());
    }

}
