package gaj.analysis.optimiser;

public enum LineSearchStatus {

    /**
     * The search successfully terminated by updating the model parameters and
     * recomputing the optimisation score.
     */
    SUCCESSFUL(OptimisationStatus.RUNNING),

    /**
     * The search terminated unsuccessfully because a feasible model parameter
     * update was not found within the specified number of iterations.
     */
    MAX_ITERATIONS_EXCEEDED(OptimisationStatus.MAX_SUB_ITERATIONS_EXCEEDED),

    /**
     * The search terminated unsuccessfully because it is known that the
     * direction will never lead to an improved optimisation score.
     */
    WRONG_DIRECTION(OptimisationStatus.SCORE_NOT_IMPROVED),

    /**
     * The search terminated unsuccessfully due to the magnitude of the model
     * parameters update direction being below tolerance.
     */
    DIRECTION_STEP_TOO_SMALL(OptimisationStatus.DIRECTION_STEP_TOO_SMALL),

    /**
     * The search terminated unsuccessfully due to the magnitude of the model
     * parameters update direction being above tolerance.
     */
    DIRECTION_STEP_TOO_LARGE(OptimisationStatus.DIRECTION_STEP_TOO_LARGE),

    /**
     * The search terminated unsuccessfully due to a failure to update the model
     * parameters.
     */
    PARAMETER_UPDATE_FAILED(OptimisationStatus.PARAMETER_UPDATE_FAILED),

    /**
     * The search terminated unsuccessfully because the required gradient
     * information could not be obtained.
     */
    GRADIENT_UNAVAILABLE(OptimisationStatus.GRADIENT_UNAVAILABLE),

    ;

    private final OptimisationStatus optimisationStatus;

    private LineSearchStatus(OptimisationStatus optimisationStatus) {
        this.optimisationStatus = optimisationStatus;
    }

    public OptimisationStatus getOptimisationStatus() {
        return optimisationStatus;
    }

}
