package gaj.analysis.optimiser.impl;

import gaj.analysis.model.score.ScoreInfo;
import gaj.analysis.optimiser.OptimisationState;
import gaj.analysis.optimiser.OptimisationStatus;

/**
 * Specifies a base implementation for keeping track of the iterations performed
 * during an optimisation.
 */
public class ModifiableOptimisationState implements OptimisationState {

    /** Current number of optimisation iterations performed. */
    private int numIterations = 0;

    /** Current number of sub-optimisation iterations performed. */
    private int subIterations = 0;

    /**
     * Current status of the optimisation process.
     */
    private OptimisationStatus status = OptimisationStatus.RUNNING;

    /**
     * Current optimisation and (optionally) validation scores of the model.
     */
    private double[] scores = new double[0];

    /** Current optimisation score and any other information. */
    private ScoreInfo optimisationScore;

    @Override
    public int getNumIterations() {
        return numIterations;
    }

    /**
     * Increments the number of optimisation iterations performed.
     */
    protected void incNumIterations() {
        numIterations++;
    }

    @Override
    public int getNumSubIterations() {
        return subIterations;
    }

    /**
     * Increments the number of sub-optimisation iterations performed.
     */
    protected void incNumSubIterations() {
        subIterations++;
    }

    @Override
    public double[] getScores() {
        return scores;
    }

    /**
     * Sets the optimisation and (optionally) validation scores of the model.
     * 
     * @param scores
     *            - The model score(s).
     */
    protected void setScores(double[] scores) {
        this.scores = scores;
    }

    @Override
    public ScoreInfo getScoreInfo() {
        return optimisationScore;
    }

    /**
     * Sets the current optimisation score information.
     * 
     * @param scoreInfo
     *            - The optimisation score information.
     */
    protected void setScoreInfo(ScoreInfo scoreInfo) {
        this.optimisationScore = scoreInfo;
    }

    @Override
    public OptimisationStatus getStatus() {
        return status;
    }

    /**
     * Sets the current status of the optimisation process.
     * 
     * @param status
     */
    protected void setStatus(OptimisationStatus status) {
        this.status = status;
    }

}
