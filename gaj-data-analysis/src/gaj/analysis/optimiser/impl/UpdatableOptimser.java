package gaj.analysis.optimiser.impl;

import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.ParameterisedModel;
import gaj.analysis.model.score.ModelScorer;
import gaj.analysis.model.score.ScoreInfo;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.optimiser.OptimisationResults;

/**
 * Specifies the base form of an optimiser bound to a specific model and a
 * specific scorer.
 */
public abstract class UpdatableOptimser<I, O> extends ModifiableOptimisationState {

    /** The model to be optimised. */
    private final ParameterisedModel<I, O> model;

    /**
     * The optimisation scorer.
     */
    private final ModelScorer<I, O> scorer;

    /**
     * Binds the optimisation algorithm to the given model and scorers.
     * 
     * @param model
     *            - The model to be optimised.
     * @param scorers
     *            - The data scorer used to measure model performance.
     */
    protected UpdatableOptimser(ParameterisedModel<I, O> model, ModelScorer<I, O> scorer) {
        this.model = model;
        this.scorer = scorer;
        computeScore();
    }

    /**
     * Obtains the optimisable model bound to the optimiser.
     * 
     * @return The model to be optimised.
     */
    public ParameterisedModel<I, O> getModel() {
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
    public void setModelParameters(DataVector params) {
        model.setParameters(params);
    }

    // ************************************************************************
    // Optimisation-specific interface. The information generated is only valid
    // for the current optimisation run.

    /**
     * Performs model optimisation, and updates the scores via
     * {@link #setScores}() and the number of iterations via
     * {@link #incIterations}().
     * 
     * @param info
     *            - The settings to control termination of the optimisation
     *            process.
     * @return The results of the optimisation.
     */
    public abstract OptimisationResults optimise(AuxiliaryInfo... info);

    /**
     * Computes the current optimisation and validation scores of the model.
     */
    protected void computeScore() {
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
