package gaj.analysis.optimiser.impl;

import gaj.analysis.curves.Quadratics;
import gaj.analysis.model.ScoreInfo;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.optimiser.GradientEnabled;
import gaj.analysis.optimiser.LineSearcherType;

/**
 * Implements {@link LineSearcherType#LINEAR}.
 */
public class QuadraticLineSearcher extends BaseLineSearcher {

    /**
     * Binds the line search to the updatable optimiser.
     * 
     * @param optimiser
     *            - The optimiser to be updated.
     */
    public QuadraticLineSearcher(UpdatableOptimser optimiser) {
        super(optimiser);
    }

    @Override
    protected double recomputeStepSize(double prevStepSize, DataVector direction, ScoreInfo prevScore) {
        ScoreInfo curScore = getOptimiser().getScoreInfo();
        if (prevScore instanceof GradientEnabled && curScore instanceof GradientEnabled) {
            DataVector g0 = ((GradientEnabled) prevScore).getGradient();
            DataVector g1 = ((GradientEnabled) curScore).getGradient();
            double s = Quadratics.quadraticOptimumScaling(g0, g1, direction);
            if (s > 0 && s < 1) return s * prevStepSize;
        }
        // Fall-back to simply reducing the step-size.
        return 0.5 * prevStepSize;
    }

}
