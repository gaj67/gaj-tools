package gaj.analysis.optimiser.impl;

import gaj.analysis.model.GradientVectorEnabled;
import gaj.analysis.model.ScoreInfo;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.SettableVector;
import gaj.analysis.optimiser.DirectionSearchParams;
import gaj.analysis.optimiser.DirectionSearchStatus;
import gaj.analysis.optimiser.DirectionSearcherType;

/**
 * Implements {@link DirectionSearcherType#LBFGS}.
 */
public class LBFGSDirectionSearcher extends BaseDirectionSearcher {

    private DataVector prevParams;

    /**
     * Binds the direction search to the optimiser.
     * 
     * @param optimiser
     *            - An updatable optimiser.
     * @param params
     *            - The parameters controlling each direction search.
     */
    public LBFGSDirectionSearcher(UpdatableOptimser optimiser, DirectionSearchParams params) {
        super(optimiser, params);
        prevParams = optimiser.getModelParameters();
    }

    @Override
    public DirectionSearchStatus search(SettableVector direction) {
        // Get gradient.
        ScoreInfo scoreInfo = getOptimiser().getScoreInfo();
        if (scoreInfo instanceof GradientVectorEnabled) {
            direction.set(((GradientVectorEnabled) scoreInfo).getGradient());
            // Get direction.
            return DirectionSearchStatus.AVAILABLE;
        }
        return DirectionSearchStatus.GRADIENT_UNAVAILABLE;
    }

}
