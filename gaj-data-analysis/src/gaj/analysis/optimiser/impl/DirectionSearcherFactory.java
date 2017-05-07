package gaj.analysis.optimiser.impl;

import gaj.analysis.optimiser.DirectionSearchParams;
import gaj.analysis.optimiser.DirectionSearcher;
import gaj.analysis.optimiser.DirectionSearcherType;
import gaj.analysis.optimiser.OptimisationParams;

/**
 * A factory for instantiating direction searchers.
 */
public abstract class DirectionSearcherFactory {

    private DirectionSearcherFactory() {}

    /**
     * Instantiates a direction searcher of the specified type and binds it to
     * the given optimiser.
     * 
     * @param optimiser
     *            - An updatable optimiser.
     * @param params
     *            - The parameters to control each direction search.
     * @return An instantiated direction searcher.
     */
    public static DirectionSearcher newDirectionSearcher(UpdatableOptimser optimiser, DirectionSearchParams params) {
        switch (params.getDirectionSearcherType()) {
            case GRADIENT:
                return new GradientDirectionSearcher(optimiser, params);
            case LBFGS:
                return new LBFGSDirectionSearcher(optimiser, params);
            default:
                throw new IllegalArgumentException("Unhandled direction searcher type: " + params.getDirectionSearcherType());
        }
    }

    /**
     * Provides (possibly default) parameter settings for a direction search
     * algorithm of the specified type.
     * 
     * @param params
     *            - The optimisation parameters.
     * @return The direction search parameters.
     */
    public static DirectionSearchParams getDirectionSearchParams(OptimisationParams params) {
        if (params instanceof DirectionSearchParams)
            return (DirectionSearchParams) params;
        final int dirSign = params.getDirectionSign();
        final DirectionSearcherType type = (params.getDirectionSearcherType() == null) ? DirectionSearcherType.GRADIENT : params.getDirectionSearcherType();
        // TODO Vary the default parameters depending upon type.
        return new DirectionSearchParams() {
            @Override
            public int getDirectionSign() {
                return dirSign;
            }

            @Override
            public DirectionSearcherType getDirectionSearcherType() {
                return type;
            }
        };
    }

}
