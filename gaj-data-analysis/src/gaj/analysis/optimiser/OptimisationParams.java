package gaj.analysis.optimiser;

public interface OptimisationParams {

    /**
     * Indicates whether to maximise or minimise the model score.
     * 
     * @return A value of true (or false) if the score is to be maximised (or
     *         minimised).
     */
    boolean maximiseScore();

    /**
     * Indicates the direction of score optimisation.
     * 
     * @return A positive (or negative) value corresponding to score
     *         maximisation (or minimisation).
     */
    default int optimisationDirection() {
        return maximiseScore() ? +1 : -1;
    }

    /**
     * Specifies the maximum number of update iterations to perform during
     * training.
     * 
     * @return The maximum number of iterations, or a non-positive value if
     *         there is no maximum.
     */
    int maxIterations();

    /**
     * Specifies the maximum number of minor iterations that may be performed
     * during a major (score-improving) iteration.
     * 
     * @return The maximum number of sub-iterations, or a non-positive value if
     *         there is no maximum.
     */
    int maxSubIterations();

    /**
     * Specifies the smallest difference in accuracy scores between update
     * iterations, below which training will cease.
     * 
     * @return The minimum score tolerance, or a non-positive value if tolerance
     *         is not to be checked.
     */
    double scoreTolerance();

    /**
     * Specifies the smallest relative difference in accuracy scores between
     * update iterations, below which training will cease.
     * 
     * @return The minimum relative score tolerance, or a non-positive value if
     *         tolerance is not to be checked.
     */
    double relativeScoreTolerance();

}
