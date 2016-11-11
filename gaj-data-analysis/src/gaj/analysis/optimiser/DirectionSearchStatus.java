package gaj.analysis.optimiser;

/**
 * Indicates the status of an attempt to find a feasible update direction for
 * the model parameters.
 */
public enum DirectionSearchStatus {

    /**
     * The direction searcher found a feasible direction and set it in the
     * argument vector.
     */
    AVAILABLE(OptimisationStatus.RUNNING),
    /**
     * The direction searcher could not find a feasible direction for some
     * unspecified reason.
     */
    NOT_FEASIBLE(OptimisationStatus.DIRECTION_UNAVAILABLE),
    /**
     * The direction searcher could not find a feasible direction because the
     * required gradient information could not be obtained.
     */
    GRADIENT_UNAVAILABLE(OptimisationStatus.GRADIENT_UNAVAILABLE);

    private final OptimisationStatus optimisationStatus;

    private DirectionSearchStatus(OptimisationStatus optimisationStatus) {
        this.optimisationStatus = optimisationStatus;
    }

    public OptimisationStatus getOptimisationStatus() {
        return optimisationStatus;
    }

}
