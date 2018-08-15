package gaj.analysis.optimiser;

import gaj.analysis.data.DataObject;
import gaj.analysis.data.numeric.DataNumeric;
import gaj.analysis.model.ParameterisedModel;
import gaj.analysis.model.score.DataModelScorer;

/**
 * Specifies a reusable optimiser.
 */
public interface Optimiser<I extends DataObject, O extends DataObject, T extends DataNumeric> {

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
    @SuppressWarnings("unchecked")
    OptimisationResults optimise(ParameterisedModel<T> model, OptimisationParams params, DataModelScorer<I, O>... scorers);

}
