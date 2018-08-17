package gaj.analysis.optimiser.impl;

import gaj.analysis.optimiser.OptimiserInfo;
import gaj.analysis.optimiser.searcher.LineSearchParams;
import gaj.analysis.optimiser.searcher.LineSearcher;
import gaj.analysis.optimiser.searcher.LineSearcherType;

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
     * @param params
     *            - The parameters to control each line search.
     * @return An instantiated line searcher.
     */
    public static LineSearcher newLineSearcher(UpdatableOptimser optimiser, LineSearchParams params) {
        switch (params.getLineSearcherType()) {
            case LINEAR:
                return new LinearLineSearcher(optimiser, params);
            case QUADRATIC:
                return new QuadraticLineSearcher(optimiser, params);
            case CUBIC:
                return new CubicLineSearcher(optimiser, params);
            default:
                throw new IllegalArgumentException("Unhandled line searcher type: " + params.getLineSearcherType());
        }
    }

    /**
     * Provides (possibly default) parameter settings for a line search
     * algorithm of the specified type.
     * 
     * @param params
     *            - The optimisation parameters.
     * @return The line search parameters.
     */
    public static LineSearchParams getLineSearchParams(OptimiserInfo params) {
        if (params instanceof LineSearchParams) return (LineSearchParams) params;
        final int dirSign = params.getDirectionSign();
        final LineSearcherType type = (params.getLineSearcherType() == null) ? LineSearcherType.LINEAR : params.getLineSearcherType();
        // TODO Vary the default parameters depending upon type.
        return new LineSearchParams() {
            @Override
            public int getDirectionSign() {
                return dirSign;
            }

            @Override
            public LineSearcherType getLineSearcherType() {
                return type;
            }
        };
    }

}
