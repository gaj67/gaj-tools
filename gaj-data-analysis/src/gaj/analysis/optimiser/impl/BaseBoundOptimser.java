package gaj.analysis.optimiser.impl;

import gaj.analysis.model.ModelScorer;
import gaj.analysis.model.OptimisableModel;
import gaj.analysis.model.ScoreInfo;
import gaj.analysis.optimiser.BoundOptimiser;
import gaj.analysis.optimiser.OptimisationParams;
import gaj.analysis.optimiser.OptimisationResults;
import gaj.analysis.optimiser.OptimiserStatus;

/**
 * Specifies the base form of a bound optimisation algorithm.
 */
public abstract class BaseBoundOptimser implements BoundOptimiser {

    /** The model to be optimised. */
    private final OptimisableModel model;

    /**
     * The optimisation and validation scorers.
     */
    private final ModelScorer[] scorers;

    /** Indicates the number of optimisation and validation scores. */
    private final int numScores;

    /** Current optimisation and validation scores. */
    private double[] scores;

    /** Current optimisation score and any other information. */
    private ScoreInfo optimisationScore;

    /** Current number of optimisation iterations performed. */
    private int numIterations = 0;

    /**
     * Current status of the optimisation process.
     */
    private OptimiserStatus status = OptimiserStatus.NOT_HALTED;

    /**
     * Binds the optimisation algorithm to the given model and scorers.
     * 
     * @param model
     *            - The model to be optimised.
     * @param scorers
     *            - The data scorer(s) used to measure model performance.
     */
    protected BaseBoundOptimser(OptimisableModel model, ModelScorer[] scorers) {
        this.model = model;
        this.scorers = scorers;
        numScores = scorers.length;
        if (numScores <= 0)
            throw new IllegalArgumentException("An optimisation scorer must be specified!");
        scores = new double[numScores];
        optimisationScore = computeScores(scores);
    }

    /**
     * Indicates the number of optimisation and validation scores.
     * 
     * @return The length of the scores array.
     */
    protected int numScores() {
        return numScores;
    }

    /**
     * Obtains the current number of optimisation iterations performed.
     * 
     * @return The number of iterations.
     */
    @Override
    public int numIterations() {
        return numIterations;
    }

    /**
     * Obtains the current model optimisation score information.
     * 
     * @return The optimisation score information.
     */
    @Override
    public ScoreInfo getOptimisationScore() {
        return optimisationScore;
    }

    /**
     * Obtains the current model optimisation and validation scores.
     * 
     * @return The array of scores.
     */
    @Override
    public double[] getScores() {
        return scores;
    }

    @Override
    public OptimiserStatus getStatus() {
        return status;
    }

    // ************************************************************************
    // Optimisation-specific interface. The information generated is only valid
    // for the current optimisation run.

    /**
     * Performs model optimisation, and updates the scores via
     * {@link #setScores}() and the number of iterations via
     * {@link #incIterations}().
     * 
     * @param params
     *            - The settings to control termination of the optimisation
     *            process.
     * @return The results of the optimisation.
     */
    @Override
    public abstract OptimisationResults optimise(OptimisationParams params);

    /**
     * Increments the number of optimisation iterations performed.
     */
    protected void incIterations() {
        numIterations++;
    }

    /**
     * Increments the number of optimisation iterations performed by the
     * specified amount.
     * 
     * @param numIterations
     *            - The number of additional iterations performed.
     */
    protected void incIterations(int numIterations) {
        this.numIterations += numIterations;
    }

    /**
     * Specifies the classifier optimisation and validation scores, and other
     * optimisation information.
     * 
     * @param optimisationScore
     *            - The optimisation score information.
     * @param scores
     *            - The optimisation and validation scores.
     */
    protected void setScores(ScoreInfo optimisationScore, double[] scores) {
        this.optimisationScore = optimisationScore;
        this.scores = scores;
    }

    /**
     * Computes the current optimisation and validation scores of the model on
     * the gold-standard data.
     * 
     * @param scores
     *            - The array of optimisation and validation scores.
     * @return The optimisation score information.
     */
    protected ScoreInfo computeScores(double[] scores) {
        computeValidationScores(scores);
        return computeOptimisationScore(scores);
    }

    /**
     * Computes the current optimisation score of the model.
     * 
     * @param scores
     *            - The array of optimisation and validation scores.
     * @return The optimisation score information.
     */
    protected ScoreInfo computeOptimisationScore(double[] scores) {
        ScoreInfo info = scorers[0].score(model);
        scores[0] = info.getScore();
        return info;
    }

    /**
     * Computes the current validation scores of the model on the gold-standard
     * data.
     * 
     * @param scores
     *            - The array of optimisation and validation scores.
     */
    protected void computeValidationScores(double[] scores) {
        for (int i = 1; i < scorers.length; i++)
            scores[i] = scorers[i].score(model).getScore();
    }

    /**
     * Sets the optimiser status to the given value.
     * 
     * @param status
     *            - The current status of the optimisation.
     */
    protected void setStatus(OptimiserStatus status) {
        this.status = status;
    }

}
