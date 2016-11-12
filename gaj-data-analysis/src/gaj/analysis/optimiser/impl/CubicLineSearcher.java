package gaj.analysis.optimiser.impl;

import gaj.analysis.curves.Cubics;
import gaj.analysis.model.ScoreInfo;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.optimiser.GradientEnabled;
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
     */
    public CubicLineSearcher(UpdatableOptimser optimiser) {
        super(optimiser);
    }

    @Override
    protected double recomputeStepSize(double prevStepSize, DataVector direction, ScoreInfo prevScore,
            LineSearchParams params) 
    {
        ScoreInfo curScore = getOptimiser().getScoreInfo();
        if (prevScore instanceof GradientEnabled && curScore instanceof GradientEnabled) {
            double y0 = prevScore.getScore();
            DataVector g0 = ((GradientEnabled) prevScore).getGradient();
            double y1 = curScore.getScore();
            DataVector g1 = ((GradientEnabled) curScore).getGradient();
            double s = (params.getOptimisationDirection() > 0)
                    ? Cubics.cubicMaximumScaling(y0, g0, y1, g1, prevStepSize, direction)
                    : Cubics.cubicMinimumScaling(y0, g0, y1, g1, prevStepSize, direction);
            if (s > 0 && s < 1) return s * prevStepSize;
        }
        // Fall-back to simply reducing the step-size.
        return 0.5 * prevStepSize;
    }

}
