package gaj.analysis.model.prob.marginal;

import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.prob.ProbModel;
import gaj.analysis.model.prob.ProbModelType;

/**
 * A model of the marginal probability p(x) of an observation x.
 * 
 * <I> - The input data type.
 */
public interface MarginalProbModel<I> extends ProbModel<I> {

    @Override
    default ProbModelType getProbModelType() {
        return ProbModelType.MARGINAL;
    }

    /**
     * Computes the data likelihood p(x).
     */
    @Override
    MarginalProbs process(I input, AuxiliaryInfo... info);

}
