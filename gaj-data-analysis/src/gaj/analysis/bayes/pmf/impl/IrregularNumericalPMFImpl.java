package gaj.analysis.bayes.pmf.impl;

import gaj.analysis.bayes.pmf.NumericalPMF;
import gaj.common.annotations.PackagePrivate;

@PackagePrivate class IrregularNumericalPMFImpl extends EmpiricalPMFImpl implements NumericalPMF {

    private final double lower;
    private final double upper;
    private final double[] values;

    @PackagePrivate IrregularNumericalPMFImpl(int startIndex, double[] values) {
        super(startIndex, values.length);
        if (values.length < 1)
            throw new IllegalArgumentException("At least one value must be given");
        this.values = values;
        double lower = Double.POSITIVE_INFINITY;
        double upper = Double.NEGATIVE_INFINITY;
        for (double value : values) {
            if (value < lower) lower = value;
            if (value > upper) upper = value;
        }
        this.lower = lower;
        this.upper = upper;
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
        return values[index - start()];
    }

    @Override
    public int index(double value) {
        if (value < lower) return Integer.MIN_VALUE;
        if (value > upper) return Integer.MAX_VALUE;
        double diff = Double.POSITIVE_INFINITY;
        int index = Integer.MIN_VALUE;
        for (int i = 0; i < values.length; i++) {
            double aDiff = Math.abs(value - values[i]);
            if (aDiff < diff) {
                diff = aDiff;
                index = i;
            }
        }
        return index + start();
    }

}
