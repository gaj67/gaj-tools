package gaj.analysis.model.prob.impl;

import gaj.analysis.model.DataInput;
import gaj.analysis.model.prob.DiscriminativeModel;
import gaj.analysis.model.prob.DiscriminativeOutput;
import gaj.analysis.model.prob.ProbModelType;
import gaj.analysis.numeric.matrix.DataMatrix;
import gaj.analysis.numeric.matrix.impl.MatrixFactory;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.WritableVector;

/**
 * Implements a discriminative logistic classifier of the form
 * 
 * <pre>
 *    p(c|x,theta) = exp{theta_c.x} / sum_{c'=1}^C exp{theta_c'.x}.
 * </pre>
 *
 */
public class LogisticClassifier implements DiscriminativeModel {

    private final DataMatrix params;
    private final int numClasses;
    private final int numFeatures;

    /**
     * Constructs a discriminative logistic classifier with modifiable weights
     * (initially set to zero).
     * 
     * @param numClasses
     *            - The number of classes.
     * @param numFeatures
     *            - The number of features.
     */
    public LogisticClassifier(int numClasses, int numFeatures) {
        this.numClasses = numClasses;
        this.numFeatures = numFeatures;
        this.params = MatrixFactory.newMatrix(numClasses, numFeatures);
    }

    /**
     * Constructs a discriminative logistic classifier with the given weights.
     * 
     * @param params
     *            - The CxF logistic weights matrix, theta =
     *            [theta_{c,f}]_{c=1}^{C}_{f=1}^{F}.
     */
    public LogisticClassifier(DataMatrix params) {
        this.params = params;
        this.numClasses = params.numRows();
        this.numFeatures = params.numColumns();
    }

    @Override
    public int numParameters() {
        return params.size();
    }

    @Override
    public DataVector getParameters() {
        return MatrixFactory.asVector(params);
    }

    @Override
    public DiscriminativeOutput process(DataInput x, boolean includeAuxiliary) {
        if (!(x instanceof DataVector)) {
            throw new IllegalArgumentException("DataVector input required!");
        }
        DataVector features = (DataVector) x;
        if (features.size() != numFeatures) {
            throw new IllegalArgumentException("Expected " + numFeatures + " features!");
        }
        WritableVector weights = MatrixFactory.multiply(params, features);
        // TODO Handle very small or very large weights.
        weights.set(numClasses - 1, 0);
        weights.apply(Math::exp);
        double norm = 1.0 / weights.sum();
        weights.multiply(norm);
        // TODO Handle gradient information.
        return new SimplePosteriors(weights, x, getProbModelType());
    }

    // ================================================================
    private static class SimplePosteriors implements DiscriminativeOutput {

        private WritableVector probs;
        private DataInput features;
        private ProbModelType probModelType;

        private SimplePosteriors(WritableVector probs, DataInput features, ProbModelType probModelType) {
            this.probs = probs;
            this.features = features;
            this.probModelType = probModelType;
        }

        @Override
        public ProbModelType getProbModelType() {
            return probModelType;
        }

        @Override
        public DataInput getInput() {
            return features;
        }

        @Override
        public DataVector getPosteriorProbabilities() {
            return probs;
        }

    }

}
