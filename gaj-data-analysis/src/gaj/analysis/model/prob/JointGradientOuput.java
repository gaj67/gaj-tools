package gaj.analysis.model.prob;

import gaj.analysis.numeric.matrix.DataMatrix;
import gaj.analysis.numeric.matrix.impl.MatrixFactory;
import gaj.analysis.numeric.vector.DataVector;

/**
 * Indicates output from a joint model with a computed gradient.
 */
public interface JointGradientOuput extends JointOutput, DiscriminativeGradientOuput, MarginalGradientOuput {

    /**
     * Computes the gradient, d_q/d_theta', of the joint log-likelihoods, q =
     * log p(y, x| theta), with respect to the model parameters, theta.
     * 
     * @return The C x P gradient matrix, where C is the number of categories of
     *         y and P is the number of parameters in theta.
     */
    DataMatrix getJointGradients();

    @Override
    default DataVector getDataGradient() {
        return MatrixFactory.multiply(getPosteriorProbabilities(), getJointGradients());
    }

    @Override
    default DataMatrix getPosteriorGradients() {
        return MatrixFactory.subtractFromRows(getJointGradients(), getDataGradient());
    }

}
