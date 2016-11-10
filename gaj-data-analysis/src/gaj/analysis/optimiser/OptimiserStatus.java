package gaj.analysis.optimiser;

/**
 * Indicates the reason why the optimisation process halted.
 */
public enum OptimiserStatus {

    /**
     * The optimisation process has not yet halted, and may be continued
     * further.
     */
    RUNNING,
    /**
     * The optimisation process halted due to the convergence of the
     * optimisation scores to within tolerance.
     */
    SCORE_CONVERGED,
    /**
     * The optimisation process halted due to the convergence of the relative
     * optimisation scores to within tolerance.
     */
    RELATIVE_SCORE_CONVERGED,
    /**
     * The optimisation process halted due to the magnitude of the optimisation
     * score gradient being below tolerance.
     */
    GRADIENT_TOO_SMALL,
    /**
     * The optimisation process halted due to the number of iterations exceeding
     * the prescribed maximum.
     */
    MAX_ITERATIONS_EXCEEDED,
    /**
     * The optimisation process halted due to the number of sub-iterations
     * exceeding the prescribed maximum.
     */
    MAX_SUB_ITERATIONS_EXCEEDED,
    /**
     * The optimisation process halted due to a failure to update the model
     * parameters.
     */
    UPDATE_FAILED,
    /**
     * The optimisation process halted due to the optimisation score not
     * improving within a major iteration.
     */
    SCORE_NOT_IMPROVED;

}
