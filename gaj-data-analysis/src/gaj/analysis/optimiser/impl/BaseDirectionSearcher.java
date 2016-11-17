package gaj.analysis.optimiser.impl;

import gaj.analysis.optimiser.DirectionSearchParams;
import gaj.analysis.optimiser.DirectionSearcher;
import gaj.analysis.optimiser.DirectionSearcherType;

/**
 * Implements {@link DirectionSearcherType#GRADIENT}.
 */
public abstract class BaseDirectionSearcher implements DirectionSearcher {

    private final UpdatableOptimser optimiser;
    private final DirectionSearchParams params;

    /**
     * Binds the direction search to the optimiser.
     * 
     * @param optimiser
     *            - An updatable optimiser.
     * @param params
     *            - The parameters to control each direction search.
     */
    public BaseDirectionSearcher(UpdatableOptimser optimiser, DirectionSearchParams params) {
        this.optimiser = optimiser;
        this.params = params;
    }

    /**
     * Obtains the underlying updatable optimiser.
     * 
     * @return The optimiser bound to this direction searcher.
     */
    protected UpdatableOptimser getOptimiser() {
        return optimiser;
    }

    /**
     * Obtains the parameters controlling each direction search.
     * 
     * @return The parameters bound to this direction searcher.
     */
    protected DirectionSearchParams getParams() {
        return params;
    }

}
