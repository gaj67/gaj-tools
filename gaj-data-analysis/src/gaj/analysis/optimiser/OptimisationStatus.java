package gaj.analysis.optimiser;

/**
 * Indicates either that the optimisation process is still running, or provides
 * the reason why the process halted.
 */
public enum OptimisationStatus {

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
    PARAMETER_UPDATE_FAILED,

    /**
     * The optimisation process halted due to the optimisation score not
     * improving within a major iteration.
     */
    SCORE_NOT_IMPROVED,

    /**
     * The optimisation process halted because the required gradient information
     * could not be obtained.
     */
    GRADIENT_UNAVAILABLE,

    /**
     * The optimisation process halted because no model parameters update
     * direction could be found.
     */
    DIRECTION_UNAVAILABLE,

    /**
     * The optimisation process halted due to the magnitude of the model
     * parameters update being below tolerance.
     */
    DIRECTION_STEP_TOO_SMALL,

    /**
     * The optimisation process halted due to the magnitude of the model
     * parameters update being above tolerance.
     */
    DIRECTION_STEP_TOO_LARGE,

    /**
     * The optimisation process halted due to some unspecified problem.
     */
    HALTED_WITH_ERROR,

    ;

}
