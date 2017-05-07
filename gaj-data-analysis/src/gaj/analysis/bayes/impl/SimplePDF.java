package gaj.analysis.bayes.impl;

import gaj.analysis.bayes.PDF;
import gaj.common.annotations.PackagePrivate;

/**
 * Basic implementation of a PDF.
 */
@PackagePrivate class SimplePDF implements PDF {

    private final int numElements;
    private final double[] probs;

    @PackagePrivate
    SimplePDF(int numElements) {
        this.numElements = numElements;
        this.probs = new double[numElements];
    }

    @Override
    public int size() {
        return numElements;
    }

    @Override
    public double get(int index) {
        return (index >= 0 && index < numElements) ? probs[index] : 0;
    }

    @Override
    public void set(int index, double value) {
        if (index >= 0 && index < numElements) probs[index] = value;
    }

    @Override
    public void add(int index, double value) {
        if (index >= 0 && index < numElements) probs[index] += value;
    }

    @Override
    public void mult(int index, double value) {
        if (index >= 0 && index < numElements) probs[index] *= value;
    }

    @Override
    public void normalise() {
        double sum = 0;
        for (int i = 0; i < numElements; i++) {
            if (probs[i] < 0) throw new IllegalStateException("Element " + i + " is negative: " + probs[i]);
            sum += probs[i];
        }
        if (sum > 0) {
            double norm = 1 / sum;
            for (int i = 0; i < numElements; i++) probs[i] *= norm;
        } else {
            double norm = 1 / numElements;
            for (int i = 0; i < numElements; i++) probs[i] = norm;
        }
    }

}
