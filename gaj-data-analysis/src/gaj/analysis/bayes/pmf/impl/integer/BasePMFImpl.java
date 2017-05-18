package gaj.analysis.bayes.pmf.impl.integer;

import gaj.analysis.bayes.pmf.EmpiricalPMF;
import gaj.common.annotations.PackagePrivate;

@PackagePrivate abstract class BasePMFImpl<T> implements EmpiricalPMF<T> {

    private final double[] probs;

    protected BasePMFImpl(int size) {
        probs = new double[size];
    }

    public Integer size() {
        return probs.length;
    }

    /**
     * Determines the internal index j = i-1 of a value x_i in X.
     * 
     * @param value
     *            - The value, x.
     * @return The value j if x = x_i in X, else the value -1.
     */
    protected abstract int index(T value);

    @Override
    public double prob(T value) {
        int index = index(value);
        return (index < 0) ? 0 : probs[index];
    }

    @Override
    public void set(T value, double prob) {
        int index = index(value);
        if (index >= 0)
            probs[index] = prob;
    }

    @Override
    public void add(T value, double probInc) {
        int index = index(value);
        if (index >= 0)
            probs[index] += probInc;
    }

    @Override
    public void mult(T value, double probScale) {
        int index = index(value);
        if (index >= 0)
            probs[index] *= probScale;
    }

    @Override
    public void normalise() {
        double sum = 0;
        for (int i = 0; i < probs.length; i++) {
            if (probs[i] < 0)
                throw new IllegalStateException("Element " + i + " is negative: " + probs[i]);
            sum += probs[i];
        }
        if (sum > 0) {
            double norm = 1.0 / sum;
            for (int i = 0; i < probs.length; i++)
                probs[i] *= norm;
        } else {
            double norm = 1.0 / probs.length;
            for (int i = 0; i < probs.length; i++)
                probs[i] = norm;
        }
    }

}
