package gaj.analysis.optimiser;

import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.ParameterisedModel;
import gaj.analysis.model.score.ModelScorer;

/**
 * Specifies a reusable model optimiser.
 * 
 * <I> - The input data type.
 * <O> - The output data type.
 */
public interface ModelOptimiser<I, O> {

    /**
     * Sets the model parameters to the value that optimises the score.
     * 
     * @param model
     *            - The model to be optimised.
     * @param scorer
     *            - A scorer by which to score the model.
     * @param info
     *            - Optional auxiliary information.
     * @return The results of the optimisation process.
     */
    OptimisationResults optimise(ParameterisedModel<I, O> model, ModelScorer<I, O> scorer, AuxiliaryInfo... info);

}
