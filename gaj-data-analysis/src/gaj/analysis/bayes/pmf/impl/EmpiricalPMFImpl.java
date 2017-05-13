package gaj.analysis.bayes.pmf.impl;

import gaj.analysis.bayes.pmf.EmpiricalPMF;
import gaj.common.annotations.PackagePrivate;

@PackagePrivate class EmpiricalPMFImpl implements EmpiricalPMF {

    private final int startIndex;
    private final int endIndex;
    private final int numElements;
    private final double[] probs;

    @PackagePrivate EmpiricalPMFImpl(int startIndex, int numElements) {
        this.startIndex = startIndex;
        this.numElements = numElements;
        this.endIndex = startIndex + numElements - 1;
        this.probs = new double[numElements];
    }
    
    @Override
    public int start() {
        return startIndex;
    }

    @Override
    public int end() {
        return endIndex;
    }

    @Override
    public int size() {
        return numElements;
    }

    @Override
    public double prob(int index) {
        return (startIndex <= index && index <= endIndex) ? probs[index - startIndex] : 0;
    }

    @Override
    public void set(int index, double value) {
        if (startIndex <= index && index <= endIndex) probs[index - startIndex] = value;
    }

    @Override
    public void add(int index, double value) {
        if (startIndex <= index && index <= endIndex) probs[index - startIndex] += value;
    }

    @Override
    public void mult(int index, double value) {
        if (startIndex <= index && index <= endIndex) probs[index - startIndex] *= value;
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
