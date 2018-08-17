package gaj.analysis.optimiser.impl;

import gaj.analysis.curves.Quadratics;
import gaj.analysis.model.VectorGradientComputable;
import gaj.analysis.model.score.ScoreInfo;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.optimiser.searcher.LineSearchParams;
import gaj.analysis.optimiser.searcher.LineSearcherType;

/**
 * Implements {@link LineSearcherType#QUADRATIC}.
 */
public class QuadraticLineSearcher extends BaseLineSearcher {

    /**
     * Binds the line search to the updatable optimiser.
     * 
     * @param optimiser
     *            - The optimiser to be updated.
     * @param params
     *            - The parameters controlling the termination of the line
     *            search.
     */
    public QuadraticLineSearcher(UpdatableOptimser optimiser, LineSearchParams params) {
        super(optimiser, params);
    }

    @Override
    protected double recomputeStepSize(double prevStepSize, DataVector direction, ScoreInfo prevScore) 
    {
        ScoreInfo curScore = getOptimiser().getScoreInfo();
        if (prevScore instanceof VectorGradientComputable && curScore instanceof VectorGradientComputable) {
            DataVector g0 = ((VectorGradientComputable) prevScore).getGradient();
            DataVector g1 = ((VectorGradientComputable) curScore).getGradient();
            double s = Quadratics.quadraticOptimumScaling(g0, g1, direction);
            if (s > 0 && s < 1) return s * prevStepSize;
        }
        // Fall-back to simply reducing the step-size.
        return 0.5 * prevStepSize;
    }

}
