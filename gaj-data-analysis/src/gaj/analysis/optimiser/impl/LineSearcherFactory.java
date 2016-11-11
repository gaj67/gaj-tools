package gaj.analysis.optimiser.impl;

import com.sun.istack.internal.Nullable;
import gaj.analysis.optimiser.LineSearchParams;
import gaj.analysis.optimiser.LineSearcher;
import gaj.analysis.optimiser.LineSearcherType;
import gaj.analysis.optimiser.OptimisationParams;

/**
 * A factory for instantiating line searchers.
 */
public abstract class LineSearcherFactory {

    private LineSearcherFactory() {}

    /**
     * Instantiates a line searcher of the specified type and binds it to the
     * given optimiser.
     * 
     * @param optimiser
     *            - An updatable optimiser.
     * @param type
     *            - The type of line searcher required.
     * @return An instantiated line searcher.
     */
    public static LineSearcher newLineSearcher(UpdatableOptimser optimiser, @Nullable LineSearcherType type) {
        if (type == null) {
            // Assume the default algorithm (this might change over time).
            return new LinearLineSearcher(optimiser);
        }
        switch (type) {
            case LINEAR:
                return new LinearLineSearcher(optimiser);
            default:
                throw new IllegalArgumentException("Unhandled line searcher type: " + type);
        }
    }

    /**
     * Provides (possibly default) parameter settings for a line search
     * algorithm of the specified type.
     * 
     * @param params
     *            - The optimisation parameters.
     * @param type
     *            - The type of line search algorithm.
     * @return The line search parameters.
     */
    public static LineSearchParams getLineSearchParams(OptimisationParams params, @Nullable LineSearcherType type) {
        if (params instanceof LineSearchParams) return (LineSearchParams) params;
        final int optimisationDirection = params.getOptimisationDirection();
        // TODO Vary the default parameters depending upon type.
        return new LineSearchParams() {
            @Override
            public int getOptimisationDirection() {
                return optimisationDirection;
            }
        };
    }

}
