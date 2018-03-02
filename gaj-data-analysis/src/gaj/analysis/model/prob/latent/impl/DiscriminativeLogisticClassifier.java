package gaj.analysis.model.prob.latent.impl;

import gaj.analysis.data.numeric.matrix.DataMatrix;
import gaj.analysis.data.numeric.matrix.impl.MatrixFactory;
import gaj.analysis.data.numeric.vector.DataVector;
import gaj.analysis.data.numeric.vector.WritableVector;
import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.DataInputOutput;
import gaj.analysis.model.prob.latent.DiscriminativeDataModel;
import gaj.analysis.model.prob.latent.DiscriminativeDataObject;
import gaj.analysis.model.prob.latent.ProbModelType;

/**
 * Implements a discriminative logistic classifier of the form
 * 
 * <pre>
 *    p(c|x,theta) = exp{theta_c.x} / sum_{c'=1}^C exp{theta_c'.x}.
 * </pre>
 *
 */
public class DiscriminativeLogisticClassifier implements DiscriminativeDataModel<DataVector> {

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
    public DiscriminativeLogisticClassifier(int numClasses, int numFeatures) {
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
    public DiscriminativeLogisticClassifier(DataMatrix params) {
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
    public boolean setParameters(DataVector params) {
        if (params.size() != numClasses * numFeatures) {
            throw new IllegalArgumentException("Expected " + numClasses * numFeatures + " parameter values!");
        }
        // TODO Refactor vector as a matrix.
        return false;
    }

    @Override
    public DiscriminativeDataObject process(DataVector features, AuxiliaryInfo... info) {
        if (features.size() != numFeatures) {
            throw new IllegalArgumentException("Expected " + numFeatures + " features!");
        }
        WritableVector weights = MatrixFactory.multiply(params, features);
        // TODO Handle very small or very large weights.
        weights.apply(Math::exp);
        double sum = weights.sum();
        if (sum == 0) {
            weights.set(1.0 / numClasses);
        } else {
            weights.multiply( 1.0 / sum);
        }
        // TODO Handle gradient information.
        return new SimplePosteriors(weights, features);
    }

    // ================================================================
    private static class SimplePosteriors implements DiscriminativeDataObject, DataInputOutput<DataVector> {

        private DataVector probs;
        private DataVector features;

        private SimplePosteriors(DataVector probs, DataVector features) {
            this.probs = probs;
            this.features = features;
        }

        @Override
        public ProbModelType getProbModelType() {
            return ProbModelType.DISCRIMINATIVE;
        }

        @Override
        public DataVector getPosteriorProbabilities() {
            return probs;
        }

        @Override
        public DataVector getData() {
            return features;
        }

    }

}
