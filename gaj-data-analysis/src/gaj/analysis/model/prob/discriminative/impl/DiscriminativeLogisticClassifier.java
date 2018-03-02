package gaj.analysis.model.prob.discriminative.impl;

import gaj.analysis.data.numeric.matrix.DataMatrix;
import gaj.analysis.data.numeric.matrix.WritableMatrix;
import gaj.analysis.data.numeric.matrix.impl.MatrixFactory;
import gaj.analysis.data.numeric.vector.DataVector;
import gaj.analysis.data.numeric.vector.WritableVector;
import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.prob.discriminative.DiscriminativeDataGradient;
import gaj.analysis.model.prob.discriminative.DiscriminativeDataModel;
import gaj.analysis.model.prob.discriminative.DiscriminativeDataObject;

/**
 * Implements a discriminative logistic classifier of the form
 * 
 * <pre>
 *    p(c|x,theta) = exp{theta_c.x} / sum_{c'=1}^C exp{theta_c'.x}.
 * </pre>
 *
 */
public class DiscriminativeLogisticClassifier extends MatrixAsVectorModel
        implements DiscriminativeDataModel<DataVector> {

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
        super(numClasses, numFeatures);
    }

    /**
     * Constructs a discriminative logistic classifier with the given weights.
     * 
     * @param params
     *            - The C x F logistic weights matrix, theta =
     *            [theta_{c,f}]_{c=1}^{C}_{f=1}^{F}.
     */
    public DiscriminativeLogisticClassifier(DataMatrix params) {
        super(params);
    }

    @Override
    public DiscriminativeDataObject process(DataVector features, AuxiliaryInfo... info) {
        if (features.size() != params.numColumns()) {
            throw new IllegalArgumentException("Expected " + params.numColumns() + " features");
        }
        final WritableVector weights = MatrixFactory.multiply(params, features);
        // TODO Handle very small or very large weights.
        weights.apply(Math::exp);
        double sum = weights.sum();
        if (sum == 0) {
            weights.set(1.0 / params.numRows());
        } else {
            weights.multiply( 1.0 / sum);
        }
        if (isGradientComputed(info)) {
            WritableMatrix gradient = MatrixFactory.newMatrix(params.numRows(), params.size());
            // TODO Compute gradient information.
            return DiscriminativeDataGradient.newDataObject(weights, gradient);
        }
        return DiscriminativeDataObject.newDataObject(weights);
    }

}
