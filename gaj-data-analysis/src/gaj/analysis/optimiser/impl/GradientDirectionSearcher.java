package gaj.analysis.optimiser.impl;

import gaj.analysis.model.VectorGradientComputable;
import gaj.analysis.model.ScoreInfo;
import gaj.analysis.numeric.vector.SettableVector;
import gaj.analysis.optimiser.DirectionSearchParams;
import gaj.analysis.optimiser.DirectionSearchStatus;
import gaj.analysis.optimiser.DirectionSearcherType;

/**
 * Implements {@link DirectionSearcherType#GRADIENT}.
 */
public class GradientDirectionSearcher extends BaseDirectionSearcher {

    /**
     * Binds the direction search to the optimiser.
     * 
     * @param optimiser
     *            - An updatable optimiser.
     * @param params
     *            - The parameters to control each direction search.
     */
    public GradientDirectionSearcher(UpdatableOptimser optimiser, DirectionSearchParams params) {
        super(optimiser, params);
    }

    @Override
    public DirectionSearchStatus search(SettableVector direction) {
        ScoreInfo scoreInfo = getOptimiser().getScoreInfo();
        if (scoreInfo instanceof VectorGradientComputable) {
            direction.set(((VectorGradientComputable) scoreInfo).getGradient());
            return DirectionSearchStatus.AVAILABLE;
        }
        return DirectionSearchStatus.GRADIENT_UNAVAILABLE;
    }

}
