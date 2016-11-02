package gaj.analysis.classifier;

import gaj.analysis.data.classifier.DatumScore;
import gaj.analysis.data.classifier.ParameterisedClassifier;
import gaj.analysis.data.vector.DataVector;

/**
 * Offers a base class for a parameterised classifier.
 * By default, no gradient is computed.
 */
public abstract class BaseClassifier implements ParameterisedClassifier {

    /** Total number C of classes. */
    protected final int numClasses;
    /** Number F of features in a data vector. */
    protected final int numFeatures;

    protected BaseClassifier(int numClasses, int numFeatures) {
        this.numClasses = numClasses;
        this.numFeatures = numFeatures;
    }

    @Override
    public int numClasses() {
        return numClasses;
    }

    @Override
    public int numFeatures() {
        return numFeatures;
    }

    @Override
    public boolean hasGradient() {
        return false;
    }

    @Override
    public DataVector getGradient(DatumScore datumScore) {
        throw new IllegalStateException("No gradient was computed");
    }
}
