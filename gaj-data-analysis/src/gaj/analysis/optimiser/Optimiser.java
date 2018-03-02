package gaj.analysis.optimiser;

import gaj.analysis.model.OptimisableModel;
import gaj.analysis.model.score.DataModelScorer;

/**
 * Specifies a reusable optimiser.
 */
public interface Optimiser {

    /**
     * Sets the model parameters to the value that optimises the score.
     * 
     * @param model
     *            - The model to be optimised.
     * @param params
     *            - The parameters controlling the optimisation process.
     * @param scorers
     *            - One or more scorers by which to score the model. If more
     *            than one scorer is given, then only the score from the first
     *            scorer is optimised.
     * @return The results of the optimisation process.
     */
    OptimisationResults optimise(OptimisableModel model, OptimisationParams params, DataModelScorer... scorers);

}
