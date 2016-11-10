package gaj.analysis.optimiser.impl;

import java.util.Arrays;
import gaj.analysis.model.ModelScorer;
import gaj.analysis.model.OptimisableModel;
import gaj.analysis.model.ScoreInfo;
import gaj.analysis.optimiser.OptimisationParams;
import gaj.analysis.optimiser.OptimisationResults;
import gaj.analysis.optimiser.OptimisationStatus;

/**
 * Specifies the basis for a reusable scheme for iterative optimisation. Each
 * call to {@link #optimise}() takes up where the previous call left off.
 */
public abstract class IterativeOptimiser extends BaseBoundOptimser {

    /**
     * Specifies the total number of iterations already performed at the start
     * of the current round of optimisation.
     */
    private int baseIterations = 0;

    /**
     * Specifies the total number of sub-iterations already performed at the
     * start of the current round of optimisation.
     */
    private int baseSubIterations = 0;

    protected IterativeOptimiser(OptimisableModel model, ModelScorer[] scorers) {
        super(model, scorers);
    }

    /**
     * Obtains the number of iterations performed during the current round of
     * optimisation.
     * 
     * @return The relative number of iterations.
     */
    protected int relIterations() {
        return numIterations() - baseIterations;
    }

    /**
     * Obtains the number of sub-iterations performed during the current round
     * of optimisation.
     * 
     * @return The relative number of sub-iterations.
     */
    protected int relSubIterations() {
        return numSubIterations() - baseSubIterations;
    }

    @Override
    public OptimisationResults optimise(OptimisationParams params) {
        final double[] initialScores = Arrays.copyOf(getScores(), numScores());
        baseIterations = numIterations();
        baseSubIterations = numSubIterations();
        OptimisationStatus status = start(params);
        setStatus(status);
        while (status == OptimisationStatus.RUNNING) {
            status = iterate(params);
            setStatus(status);
        }
        final double[] finalScores = Arrays.copyOf(getScores(), numScores());
        return end(getResults(relIterations(), relSubIterations(), initialScores, finalScores, getScoreInfo(), status));
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
        OptimisationStatus status = preUpdate(params);
        if (status != OptimisationStatus.RUNNING)
            return status;
        double[] prevScores = Arrays.copyOf(getScores(), numScores());
        status = update(params);
        if (status != OptimisationStatus.RUNNING)
            return status;
        incIterations();
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
        if (params.maxIterations() > 0 && relIterations() >= params.maxIterations())
            return OptimisationStatus.MAX_ITERATIONS_EXCEEDED;
        if (params.maxSubIterations() > 0 && relSubIterations() >= params.maxSubIterations())
            return OptimisationStatus.MAX_SUB_ITERATIONS_EXCEEDED;
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
        final double diffScore = params.optimisationDirection() * (getScores()[0] - prevScores[0]);
        if (diffScore <= 0)
            return OptimisationStatus.SCORE_NOT_IMPROVED;
        if (params.scoreTolerance() > 0 && diffScore < params.scoreTolerance())
            return OptimisationStatus.SCORE_CONVERGED;
        if (params.relativeScoreTolerance() > 0 && diffScore < Math.abs(prevScores[0]) * params.relativeScoreTolerance())
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
            public int numIterations() {
                return numIterations;
            }

            @Override
            public int numSubIterations() {
                return subIterations;
            }

            @Override
            public double[] initalScores() {
                return initialScores;
            }

            @Override
            public double[] finalScores() {
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
