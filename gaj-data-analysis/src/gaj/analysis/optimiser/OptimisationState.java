package gaj.analysis.optimiser;

import gaj.analysis.model.ScoreInfo;

/**
 * Summarises the state of an optimisation process.
 */
public interface OptimisationState {

    /**
     * Indicates the number of iterations performed.
     * 
     * @return The number of iterations.
     */
    int numIterations();

    /**
     * Indicates the number of sub-iterations performed.
     * 
     * @return The number of sub-iterations.
     */
    int numSubIterations();

    /**
     * Indicates the optimisation and (optionally) validation scores of the
     * model.
     * 
     * @return The model score(s).
     */
    double[] getScores();

    /**
     * Indicates the full information associated with the optimisation score,
     * e.g. possibly including gradient information, etc.
     * 
     * @return The optimisation score information.
     */
    ScoreInfo getScoreInfo();

    /**
     * Indicates the status of the optimisation process.
     * 
     * @return The optimisation status.
     */
    OptimisationStatus getStatus();

}
