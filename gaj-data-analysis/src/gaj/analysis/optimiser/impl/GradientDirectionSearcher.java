package gaj.analysis.optimiser.impl;

import gaj.analysis.model.ScoreInfo;
import gaj.analysis.numeric.vector.SettableVector;
import gaj.analysis.optimiser.DirectionSearchStatus;
import gaj.analysis.optimiser.DirectionSearcher;
import gaj.analysis.optimiser.DirectionSearcherType;
import gaj.analysis.optimiser.GradientEnabled;

/**
 * Implements {@link DirectionSearcherType#GRADIENT}.
 */
public class GradientDirectionSearcher implements DirectionSearcher {

    private final UpdatableOptimser optimiser;

    /**
     * Binds the direction search to the optimiser.
     * 
     * @param optimiser
     *            - An updatable optimiser.
     */
    public GradientDirectionSearcher(UpdatableOptimser optimiser) {
        this.optimiser = optimiser;
    }

    @Override
    public DirectionSearchStatus search(SettableVector direction) {
        ScoreInfo scoreInfo = optimiser.getScoreInfo();
        if (scoreInfo instanceof GradientEnabled) {
            direction.set(((GradientEnabled) scoreInfo).getGradient());
            return DirectionSearchStatus.AVAILABLE;
        }
        return DirectionSearchStatus.GRADIENT_UNAVAILABLE;
    }

}