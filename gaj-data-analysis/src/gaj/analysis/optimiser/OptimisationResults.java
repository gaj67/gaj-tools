package gaj.analysis.optimiser;

/**
 * Summarises the results of a single round of model optimisation. All values,
 * e.g. number of iterations, etc., are relative to the start of the
 * optimisation round.
 */
public interface OptimisationResults extends OptimisationState {

    /**
     * Indicates the optimisation and (optionally) validation scores of the
     * model prior to optimisation.
     * 
     * @return The initial score(s).
     */
    double[] getInitalScores();

    /**
     * Indicates the optimisation and (optionally) validation scores of the
     * model after optimisation.
     * 
     * @return The final score(s).
     */
    default double[] getFinalScores() {
        return getScores();
    }

}
