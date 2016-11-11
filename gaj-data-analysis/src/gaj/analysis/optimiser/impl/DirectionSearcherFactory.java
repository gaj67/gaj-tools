package gaj.analysis.optimiser.impl;

import gaj.analysis.optimiser.DirectionSearcher;
import gaj.analysis.optimiser.DirectionSearcherType;

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
     * @param type
     *            - The type of direction searcher required.
     * @return An instantiated direction searcher.
     */
    public static DirectionSearcher newDirectionSearcher(UpdatableOptimser optimiser, DirectionSearcherType type) {
        if (type == null) {
            // Assume the default algorithm (this might change over time).
            return new GradientDirectionSearcher(optimiser);
        }
        switch (type) {
            case GRADIENT:
                return new GradientDirectionSearcher(optimiser);
            default:
                throw new IllegalArgumentException("Unhandled direction searcher type: " + type);
        }
    }

}
