package gaj.analysis.optimiser;

import gaj.analysis.model.ScoreInfo;

/**
 * A reusable optimiser that is bound to a specific model and to specified
 * optimisation and (optionally) validation scorers.
 */
public interface BoundOptimiser {

    /**
     * Updates the model parameters to locally optimise the score.
     * 
     * @param params
     *            - The parameters controlling the optimisation process.
     * @return The results of the optimisation.
     */
    OptimisationResults optimise(OptimisationParams params);

    /**
     * Indicates the total number of iterations performed by the optimiser so
     * far.
     * 
     * @return The number of iterations.
     */
    int numIterations();

    /**
     * Indicates the total number of sub-optimisation iterations performed
     * across all iterations.
     * 
     * @return The number of sub-iterations.
     */
    public int numSubIterations();

    /**
     * Indicates the current status of the optimiser.
     * 
     * @return The optimiser status.
     */
    OptimiserStatus getStatus();

    /**
     * Obtains the current model optimisation score information.
     * 
     * @return The optimisation score information.
     */
    ScoreInfo getOptimisationScore();

    /**
     * Obtains the current model optimisation and validation scores.
     * 
     * @return The array of scores.
     */
    double[] getScores();

}
