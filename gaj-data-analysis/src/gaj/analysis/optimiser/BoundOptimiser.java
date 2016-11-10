package gaj.analysis.optimiser;

/**
 * A reusable optimiser that is bound to a specific model and to specified
 * optimisation and (optionally) validation scorers.
 */
public interface BoundOptimiser extends /* has-an */ OptimisationState {

    /**
     * Updates the model parameters to locally optimise the score.
     * 
     * @param params
     *            - The parameters controlling the optimisation process.
     * @return The results of the optimisation.
     */
    OptimisationResults optimise(OptimisationParams params);

}
