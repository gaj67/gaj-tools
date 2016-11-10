package gaj.analysis.optimiser.impl;

import java.util.Arrays;
import gaj.analysis.model.ModelScorer;
import gaj.analysis.model.OptimisableModel;
import gaj.analysis.optimiser.OptimisationParams;
import gaj.analysis.optimiser.OptimisationResults;
import gaj.analysis.optimiser.OptimiserStatus;

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
        OptimiserStatus status = start(params);
        setStatus(status);
        while (status == OptimiserStatus.RUNNING) {
            status = iterate(params);
            setStatus(status);
        }
        final double[] finalScores = Arrays.copyOf(getScores(), numScores());
        return end(getResults(relIterations(), relSubIterations(), initialScores, finalScores, status));
    }

    /**
     * Determines the starting status of the optimiser to commence a new round
     * of optimisation.
     * 
     * @param params
     *            - The parameters controlling the optimisation process.
     * @return The status with which to start this round of optimisation.
     */
    protected OptimiserStatus start(OptimisationParams params) {
        OptimiserStatus curStatus = getStatus();
        if (OptimiserStatus.MAX_ITERATIONS_EXCEEDED == curStatus || OptimiserStatus.MAX_SUB_ITERATIONS_EXCEEDED == curStatus)
            return OptimiserStatus.RUNNING;
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
    protected OptimiserStatus iterate(OptimisationParams params) {
        OptimiserStatus status = preUpdate(params);
        if (status != OptimiserStatus.RUNNING)
            return status;
        double[] prevScores = Arrays.copyOf(getScores(), numScores());
        status = update(params);
        if (status != OptimiserStatus.RUNNING)
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
    protected OptimiserStatus preUpdate(OptimisationParams params) {
        if (params.maxIterations() > 0 && relIterations() >= params.maxIterations())
            return OptimiserStatus.MAX_ITERATIONS_EXCEEDED;
        if (params.maxSubIterations() > 0 && relSubIterations() >= params.maxSubIterations())
            return OptimiserStatus.MAX_SUB_ITERATIONS_EXCEEDED;
        return OptimiserStatus.RUNNING;
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
    protected abstract OptimiserStatus update(OptimisationParams params);

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
    protected OptimiserStatus postUpdate(OptimisationParams params, double[] prevScores) {
        final double diffScore = params.optimisationDirection() * (getScores()[0] - prevScores[0]);
        if (diffScore <= 0)
            return OptimiserStatus.SCORE_NOT_IMPROVED;
        if (params.scoreTolerance() > 0 && diffScore < params.scoreTolerance())
            return OptimiserStatus.SCORE_CONVERGED;
        if (params.relativeScoreTolerance() > 0 && diffScore < Math.abs(prevScores[0]) * params.relativeScoreTolerance())
            return OptimiserStatus.RELATIVE_SCORE_CONVERGED;
        return OptimiserStatus.RUNNING;
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

    private OptimisationResults getResults(int diffIterations, int subIterations, double[] initialScores, double[] finalScores, OptimiserStatus status) {
        return new OptimisationResults() {
            @Override
            public int numIterations() {
                return diffIterations;
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
            public OptimiserStatus getStatus() {
                return status;
            }
        };
    }

}
