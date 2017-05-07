package gaj.analysis.optimiser;

/**
 * Specifies the parameters for a search for a model parameters update
 * direction.
 */
public interface LBFGSDirectionSearchParams extends DirectionSearchParams {

    /**
     * Indicates the number of previous direction updates to keep in memory for
     * computing the approximate inverse Hessian matrix.
     * 
     * @return The memory size.
     */
    default int getMemorySize() {
        return 10;
    }

}
