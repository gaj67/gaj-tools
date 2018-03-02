package gaj.analysis.optimiser.impl;

import java.util.Arrays;
import gaj.analysis.model.OptimisableModel;
import gaj.analysis.model.score.DataModelScorer;
import gaj.analysis.model.score.ScoreInfo;
import gaj.analysis.optimiser.OptimisationParams;
import gaj.analysis.optimiser.OptimisationResults;
import gaj.analysis.optimiser.OptimisationStatus;

/**
 * Specifies the basis for a reusable scheme for iterative optimisation. Each
 * call to {@link #optimise}() takes up where the previous call left off.
 */
public abstract class IterativeOptimiser extends UpdatableOptimser {

    /**
     * Specifies the total number of iterations already performed at the start
     * of the current round of optimisation.
     */
    private int outerIterations = 0;

    /**
     * Specifies the total number of sub-iterations already performed at the
     * start of the current round of optimisation.
     */
    private int outerSubIterations = 0;

    /**
     * Specifies the total number of sub-iterations already performed at the
     * start of the current iteration within the current round of optimisation.
     */
    private int innerSubIterations = 0;

    protected IterativeOptimiser(OptimisableModel model, DataModelScorer[] scorers) {
        super(model, scorers);
    }

    /**
     * Obtains the number of iterations performed during the current round of
     * optimisation.
     * 
     * @return The relative number of iterations.
     */
    protected int getRelativeNumIterations() {
        return getNumIterations() - outerIterations;
    }

    /**
     * Obtains the number of sub-iterations performed during the current round
     * of optimisation.
     * 
     * @return The relative number of sub-iterations.
     */
    protected int getRelativeNumSubIterations() {
        return getNumSubIterations() - outerSubIterations;
    }

    /**
     * Obtains the number of sub-iterations performed during the current
     * iteration within the current round of optimisation.
     * 
     * @return The relative number of sub-iterations.
     */
    protected int getInnerNumSubIterations() {
        return getNumSubIterations() - innerSubIterations;
    }

    @Override
    public OptimisationResults optimise(OptimisationParams params) {
        final double[] initialScores = Arrays.copyOf(getScores(), numScores());
        outerIterations = getNumIterations();
        outerSubIterations = getNumSubIterations();
        OptimisationStatus status = start(params);
        setStatus(status);
        while (status == OptimisationStatus.RUNNING) {
            status = iterate(params);
            setStatus(status);
        }
        final double[] finalScores = Arrays.copyOf(getScores(), numScores());
        return end(getResults(getRelativeNumIterations(), getRelativeNumSubIterations(), initialScores, finalScores, getScoreInfo(), status));
    }

    /**
     * Determines the starting status of the optimiser to commence a new round
     * of optimisation.
     * 
     * @param params
     *            - The parameters controlling the optimisation process.
     * @return The status with which to start this round of optimisation.
     */
    protected OptimisationStatus start(OptimisationParams params) {
        OptimisationStatus curStatus = getStatus();
        if (OptimisationStatus.MAX_ITERATIONS_EXCEEDED == curStatus || OptimisationStatus.MAX_SUB_ITERATIONS_EXCEEDED == curStatus)
            return OptimisationStatus.RUNNING;
        return curStatus;
    }

    /**
     * Attempts one major iteration of optimisation, updating the optimisation
     * and validation scores and the number of iterations.
     * 
     * @param params
     *            - The parameters controlling the optimisation process.
     * @return The status of the optimiser after the iteration.
     */
    protected OptimisationStatus iterate(OptimisationParams params) {
        innerSubIterations = getNumSubIterations();
        OptimisationStatus status = preUpdate(params);
        if (status != OptimisationStatus.RUNNING)
            return status;
        double[] prevScores = Arrays.copyOf(getScores(), numScores());
        status = update(params);
        if (status != OptimisationStatus.RUNNING)
            return status;
        incNumIterations();
        computeValidationScores();
        return postUpdate(params, prevScores);
    }

    /**
     * Checks whether or not optimisation should cease prior to a model
     * parameter update.
     * 
     * @param params
     *            - The parameters controlling the optimisation process.
     * @return The status of the optimiser before commencing an update.
     */
    protected OptimisationStatus preUpdate(OptimisationParams params) {
        if (params.getMaxIterations() > 0 && getRelativeNumIterations() >= params.getMaxIterations())
            return OptimisationStatus.MAX_ITERATIONS_EXCEEDED;
        return OptimisationStatus.RUNNING;
    }

    /**
     * Performs an update of the model parameters. If successful, this method is
     * responsible for updating the optimisation score and number of
     * sub-iterations performed.
     * 
     * @param params
     *            - The parameters controlling the optimisation process.
     * @return The status of the optimiser after attempting an update.
     */
    protected abstract OptimisationStatus update(OptimisationParams params);

    /**
     * Checks whether or not optimisation should cease based on the change in
     * scores due to a model parameter update. For example, validation scores
     * could be used to control over-training.
     * 
     * @param params
     *            - The parameters controlling the optimisation process.
     * @param prevScores
     *            - The optimisation and validation scores before the update.
     * @return The status of the optimiser after successfully performing an
     *         update.
     */
    protected OptimisationStatus postUpdate(OptimisationParams params, double[] prevScores) {
        final double diffScore = params.getDirectionSign() * (getScores()[0] - prevScores[0]);
        if (diffScore <= 0)
            return OptimisationStatus.SCORE_NOT_IMPROVED;
        if (params.getScoreTolerance() > 0 && diffScore < params.getScoreTolerance())
            return OptimisationStatus.SCORE_CONVERGED;
        if (params.getRelativeScoreTolerance() > 0 && diffScore < Math.abs(prevScores[0]) * params.getRelativeScoreTolerance())
            return OptimisationStatus.RELATIVE_SCORE_CONVERGED;
        return OptimisationStatus.RUNNING;
    }

    /**
     * Produces a summary of the current round of optimisation.
     * 
     * @param results
     *            - A simple summary of the current round of optimisation.
     * @return The optimisation results.
     */
    protected OptimisationResults end(OptimisationResults results) {
        return results;
    }

    private OptimisationResults getResults(int numIterations, int subIterations, double[] initialScores, double[] finalScores, ScoreInfo scoreInfo, OptimisationStatus status) {
        return new OptimisationResults() {
            @Override
            public int getNumIterations() {
                return numIterations;
            }

            @Override
            public int getNumSubIterations() {
                return subIterations;
            }

            @Override
            public double[] getInitalScores() {
                return initialScores;
            }

            @Override
            public double[] getFinalScores() {
                return finalScores;
            }

            @Override
            public OptimisationStatus getStatus() {
                return status;
            }

            @Override
            public double[] getScores() {
                return finalScores;
            }

            @Override
            public ScoreInfo getScoreInfo() {
                return scoreInfo;
            }
        };
    }

}
