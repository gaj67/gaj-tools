package gaj.analysis.optimiser;

import gaj.analysis.model.score.ScoreInfo;

/**
 * Summarises the results of a single round of model optimisation. All values,
 * e.g. number of iterations, etc., are relative to the start of the
 * optimisation round.
 */
public interface OptimisationResults extends OptimisationState {

    /**
     * Indicates the score of the model prior to optimisation.
     * 
     * @return The initial score.
     */
    ScoreInfo getInitalScore();

    /**
     * Indicates the score of the model after optimisation.
     * 
     * @return The final score.
     */
    default ScoreInfo getFinalScore() {
        return getScore();
    }

}
