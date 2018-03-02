package gaj.analysis.optimiser.impl;

import gaj.analysis.data.numeric.vector.DataVector;
import gaj.analysis.model.OptimisableModel;
import gaj.analysis.model.score.ModelScorer;
import gaj.analysis.model.score.ScoreInfo;
import gaj.analysis.optimiser.BoundOptimiser;
import gaj.analysis.optimiser.OptimisationParams;
import gaj.analysis.optimiser.OptimisationResults;

/**
 * Specifies the base form of a bound optimisation algorithm.
 */
public abstract class UpdatableOptimser extends ModifiableOptimisationState implements BoundOptimiser {

    private static final boolean WITH_AUXILIARY = true;
    private static final boolean NO_AUXILIARY = false;

    /** The model to be optimised. */
    private final OptimisableModel model;

    /**
     * The optimisation and validation scorers.
     */
    private final ModelScorer[] scorers;

    /** Indicates the number of optimisation and validation scores. */
    private final int numScores;

    /**
     * Binds the optimisation algorithm to the given model and scorers.
     * 
     * @param model
     *            - The model to be optimised.
     * @param scorers
     *            - The data scorer(s) used to measure model performance.
     */
    protected UpdatableOptimser(OptimisableModel model, ModelScorer[] scorers) {
        this.model = model;
        this.scorers = scorers;
        numScores = scorers.length;
        if (numScores <= 0)
            throw new IllegalArgumentException("An optimisation scorer must be specified!");
        setScores(new double[numScores]);
        computeScores();
    }

    /**
     * Obtains the optimisable model bound to the optimiser.
     * 
     * @return The model to be optimised.
     */
    public OptimisableModel getModel() {
        return model;
    }

    /**
     * Obtains the current parameters of the model.
     * 
     * @return The model parameters
     */
    public DataVector getModelParameters() {
        return model.getParameters();
    }

    /**
     * Sets the current parameters of the model.
     * 
     * @param params
     *            - The model parameters
     * @return A value of true (or false) if the model parameters were (or were
     *         not) successfully updated.
     */
    public boolean setModelParameters(DataVector params) {
        return model.setParameters(params);
    }

    /**
     * Indicates the number of optimisation and validation scores.
     * 
     * @return The length of the scores array.
     */
    protected int numScores() {
        return numScores;
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
     * Computes the current optimisation and validation scores of the model.
     */
    protected void computeScores() {
        computeValidationScores();
        computeOptimisationScore();
    }

    /**
     * Computes the current optimisation score of the model.
     */
    protected void computeOptimisationScore() {
        ScoreInfo optimisationScore = scorers[0].score(model, WITH_AUXILIARY);
        setScoreInfo(optimisationScore);
        getScores()[0] = optimisationScore.getScore();
    }

    /**
     * Computes the current validation scores of the model.
     */
    protected void computeValidationScores() {
        double[] scores = getScores();
        for (int i = 1; i < scorers.length; i++) {
            scores[i] = scorers[i].score(model, NO_AUXILIARY).getScore();
        }
    }

}
