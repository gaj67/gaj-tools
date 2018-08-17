package gaj.analysis.model.prob.discriminative.impl;

import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.GradientAware;
import gaj.analysis.model.prob.discriminative.DiscriminativeProbsGradient;
import gaj.analysis.model.prob.discriminative.DiscriminativeProbModel;
import gaj.analysis.model.prob.discriminative.DiscriminativeProbs;
import gaj.analysis.numeric.matrix.DataMatrix;
import gaj.analysis.numeric.matrix.WritableMatrix;
import gaj.analysis.numeric.matrix.impl.MatrixFactory;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.WritableVector;
import gaj.analysis.numeric.vector.impl.VectorFactory;

/**
 * Implements a discriminative logistic classifier of the form
 * 
 * <pre>
 *    p(c|x,theta) = exp{theta_c.x} / sum_{c'=1}^C exp{theta_c'.x}.
 * </pre>
 *
 */
public class DiscriminativeLogisticClassifier extends MatrixAsVectorModel
    implements DiscriminativeProbModel<DataVector>, GradientAware 
{

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
    public DiscriminativeProbs process(DataVector features, AuxiliaryInfo... info) {
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
        if (computeGradient(info)) {
            DataMatrix gradient = computeGradient(weights, features);
            return DiscriminativeProbsGradient.newDataObject(weights, gradient);
        }
        return DiscriminativeProbs.newDataObject(weights);
    }

    /*
     * Computes the C x P gradient matrix [d q_c/d theta_p] for 
     * q_c = ln p(c|x,theta) and theta=vec[theta_p]=matrix[theta_c'f]. 
     * Now, q_c = exp(sum_f theta_cf*x_f) / sum_c' exp(sum_f theta_cf*x_f),
     * so, d q_c/d theta_p = x_f [delta(c,c') - p(c'|x,theta)], where p = c'F + f.
     */
    private DataMatrix computeGradient(DataVector probs, DataVector features) {
        DataVector partialGradient = MatrixFactory.asVector(MatrixFactory.multiply(VectorFactory.scale(probs, -1), features));
        final int numClasses = params.numRows();
        final int numFeatures = features.size();
        WritableMatrix gradient = MatrixFactory.newMatrix(numClasses, partialGradient.size());
        int pos = 0;
        for (int c = 0; c < numClasses; c++) {
            WritableVector row = gradient.getRow(c);
            row.set(partialGradient);
            VectorFactory.newSubVector(row, pos, numFeatures).add(features);
            pos += numFeatures;
        }
        return gradient;
    }

}
