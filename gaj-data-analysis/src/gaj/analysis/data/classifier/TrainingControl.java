package gaj.analysis.data.classifier;

/**
 * Specifies any parameter settings required for updating a trainable classifier.
 */
public interface TrainingControl {

    /**
     * Specifies the maximum number of update iterations to perform during training.
     * 
     * @return The maximum number of iterations, or a non-positive value if there is no maximum.
     */
    int maxIterations();

    /**
     * Specifies the maximum number of minor iterations that may be performed during
     * a major (score-improving) iteration.
     * 
     * @return The maximum number of sub-iterations, or a non-positive value if there is no maximum.
     */
    int maxSubIterations();

    /**
     * Specifies the smallest difference in accuracy scores between update iterations,
     * below which training will cease.
     * 
     * @return The minimum score tolerance.
     */
    double scoreTolerance();

    /**
     * Specifies the smallest relative difference in accuracy scores between update iterations,
     * below which training will cease.
     * 
     * @return The minimum relative score tolerance.
     */
    double relativeScoreTolerance();

    /**
     * Specifies the smallest feasible norm of the score gradient, below which
     * training will cease. This will only be checked if classifier.hasGradient() is true.
     * 
     * @return The minimum score gradient tolerance.
     */
    double gradientTolerance();

}
