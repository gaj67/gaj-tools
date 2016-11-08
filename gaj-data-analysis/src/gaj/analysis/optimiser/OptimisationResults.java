package gaj.analysis.optimiser;

/**
 * Summarises the iterative parameters updates performed during model
 * optimisation.
 */
public interface OptimisationResults {

    /**
     * Indicates the number of update iterations actually performed during the
     * latest round of optimisation.
     * 
     * @return The number of iterations.
     */
    int numIterations();

    /**
     * Indicates the total number of update sub-iterations actually performed
     * during the latest round of optimisation.
     * 
     * @return The total number of sub-iterations.
     */
    int numSubIterations();

    /**
     * Indicates the optimisation and (optionally) validation scores of the
     * model prior to optimisation.
     * 
     * @return The initial score(s).
     */
    double[] initalScores();

    /**
     * Indicates the optimisation and (optionally) validation scores of the
     * model after optimisation.
     * 
     * @return The final score(s).
     */
    double[] finalScores();

    /**
     * Indicates the status of the optimiser after the optimisation process.
     * 
     * @return The optimiser status.
     */
    OptimiserStatus getStatus();

}
