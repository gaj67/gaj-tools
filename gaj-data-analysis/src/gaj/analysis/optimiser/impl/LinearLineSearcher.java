package gaj.analysis.optimiser.impl;

import gaj.analysis.model.ScoreInfo;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.optimiser.LineSearcherType;

/**
 * Implements {@link LineSearcherType#LINEAR}.
 */
public class LinearLineSearcher extends BaseLineSearcher {

    /**
     * Binds the line search to the updatable optimiser.
     * 
     * @param optimiser
     *            - The optimiser to be updated.
     */
    public LinearLineSearcher(UpdatableOptimser optimiser) {
        super(optimiser);
    }

    @Override
    protected double recomputeStepSize(double prevStepSize, DataVector direction, ScoreInfo prevScore) {
        return prevStepSize * 0.5;
    }

}
