package gaj.data.classifier;

/**
 * Indicates the reason why the training process halted.
 */
public enum TrainingState {

    /** The training process has not yet halted, and may be continued further. */
    NOT_HALTED,
    /** The training process halted due to the convergence of the training scores to within tolerance. */
    SCORE_CONVERGED,
    /** The training process halted due to the convergence of the relative training scores to within tolerance. */
    RELATIVE_SCORE_CONVERGED,
    /** The training process halted due to the magnitude of the training score gradient being below tolerance. */
    GRADIENT_TOO_SMALL,
    /** The training process halted due to the number of iterations exceeding the prescribed maximum. */
    MAX_ITERATIONS_EXCEEDED,
    /** The training process halted due to the number of sub-iterations exceeding the prescribed maximum. */
    MAX_SUB_ITERATIONS_EXCEEDED,
    /** The training process halted due to a failure to update the classifier parameters. */
    UPDATE_FAILED,
    /** The training process halted due to the training score not increasing within a major iteration. */
    SCORE_NOT_IMPROVED;

}
