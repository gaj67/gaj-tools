package gaj.analysis.optimiser.impl;

import gaj.analysis.curves.Cubics;
import gaj.analysis.data.numeric.vector.DataVector;
import gaj.analysis.model.VectorGradientComputable;
import gaj.analysis.model.score.ScoreInfo;
import gaj.analysis.optimiser.LineSearchParams;
import gaj.analysis.optimiser.LineSearcherType;

/**
 * Implements {@link LineSearcherType#CUBIC}.
 */
public class CubicLineSearcher extends BaseLineSearcher {

    /**
     * Binds the line search to the updatable optimiser.
     * 
     * @param optimiser
     *            - The optimiser to be updated.
     * @param params
     *            - The parameters controlling the termination of the line
     *            search.
     */
    public CubicLineSearcher(UpdatableOptimser optimiser, LineSearchParams params) {
        super(optimiser, params);
    }

    @Override
    protected double recomputeStepSize(double prevStepSize, DataVector direction, ScoreInfo prevScore) 
    {
        ScoreInfo curScore = getOptimiser().getScoreInfo();
        if (prevScore instanceof VectorGradientComputable && curScore instanceof VectorGradientComputable) {
            double y0 = prevScore.getScore();
            DataVector g0 = ((VectorGradientComputable) prevScore).getGradient();
            double y1 = curScore.getScore();
            DataVector g1 = ((VectorGradientComputable) curScore).getGradient();
            double s = (getParams().getDirectionSign() > 0)
                    ? Cubics.cubicMaximumScaling(y0, g0, y1, g1, prevStepSize, direction)
                    : Cubics.cubicMinimumScaling(y0, g0, y1, g1, prevStepSize, direction);
            if (s > 0 && s < 1) return s * prevStepSize;
        }
        // Fall-back to simply reducing the step-size.
        return 0.5 * prevStepSize;
    }

}
