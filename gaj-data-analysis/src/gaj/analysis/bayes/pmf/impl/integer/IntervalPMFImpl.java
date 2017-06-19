package gaj.analysis.bayes.pmf.impl.integer;

import gaj.analysis.bayes.pmf.IntervalPMF;
import gaj.common.annotations.PackagePrivate;

@PackagePrivate
class IntervalPMFImpl extends BasePMFImpl<Double> implements IntervalPMF<Double, Integer> {

    private final double lower;
    private final double upper;
    private final double step;

    @PackagePrivate IntervalPMFImpl(double lower, double upper, int size) {
        super(size);
        if (size < 2)
            throw new IllegalArgumentException("Must specify at least two values in the domain");
        this.lower = lower;
        this.step = (upper - lower) / (size - 1);
        // this.upper = upper;
        this.upper = lower + step * (size - 1);
    }

    @Override
    public Double lower() {
        return lower;
    }

    @Override
    public Double upper() {
        return upper;
    }

    @Override
    public Double step() {
        return step;
    }

    @Override
    public Double value(Integer index) {
        if (index < 0) return Double.NEGATIVE_INFINITY;
        if (index >= size()) return Double.POSITIVE_INFINITY;
        return lower + step * index;
    }

    @Override
    protected int index(Double value) {
        if (value < lower) return -1;
        if (value > upper) return -1;
        int index = (int) ((value - lower) / step);
        double aValue = lower + index * step;
        return (Math.abs(aValue - value) <= 1e-30) ? index : -1;
    }

}
