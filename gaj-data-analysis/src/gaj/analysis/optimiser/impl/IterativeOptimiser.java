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
     * Specifies the total number of sub-iterations performed by the
     * {@link #update}() method over the course of a single call to
     * {@link #optimise}().
     */
    private int subIterations = 0;

    protected IterativeOptimiser(OptimisableModel model, ModelScorer[] scorers) {
        super(model, scorers);
    }

    /**
     * Obtains the total number of sub-iterations performed by {@link #update}()
     * during the current round of optimisation.
     * 
     * @return The number of sub-iterations.
     */
    protected int numSubIterations() {
        return subIterations;
    }

    /**
     * Increments the number of sub-iterations. To be called by
     * {@link #update}().
     */
    protected void incSubIterations() {
        subIterations++;
    }

    /**
     * Increments the number of sub-iterations by the amount specified. To be
     * called by {@link #update}().
     */
    protected void incSubIterations(int increment) {
        subIterations += increment;
    }

    @Override
    public OptimisationResults optimise(OptimisationParams params) {
        final int initialIterations = numIterations();
        subIterations = 0;
        final double[] initialScores = Arrays.copyOf(getScores(), numScores());
        OptimiserStatus status = start(params, getStatus());
        setStatus(status);
        while (status == OptimiserStatus.NOT_HALTED) {
            status = iterate(params);
            setStatus(status);
        }
        final int finalIterations = numIterations();
        final double[] finalScores = Arrays.copyOf(getScores(), numScores());
        return end(getResults(finalIterations - initialIterations, subIterations, initialScores, finalScores, status));
    }

    /**
     * Determines the starting status of the optimiser to commence a new round
     * of optimisation.
     * 
     * @param params
     *            - The parameters controlling the optimisation process.
     * @param curStatus
     *            - The current status of the optimiser.
     * @return The status with which to start this round of optimisation.
     */
    protected OptimiserStatus start(OptimisationParams params, OptimiserStatus curStatus) {
        if (OptimiserStatus.MAX_ITERATIONS_EXCEEDED == curStatus || OptimiserStatus.MAX_SUB_ITERATIONS_EXCEEDED == curStatus)
            return OptimiserStatus.NOT_HALTED;
        return curStatus;
    }

    /**
     * Attempts one major iteration of optimisation, updating the scores and
     * number of iterations via {@link #setScores}() and
     * {@link #incIterations}(), respectively.
     * 
     * @param params
     *            - The parameters controlling the optimisation process.
     * @return The status of the optimiser after the iteration.
     */
    protected OptimiserStatus iterate(OptimisationParams params) {
        OptimiserStatus status = preUpdate(params);
        if (status != OptimiserStatus.NOT_HALTED)
            return status;
        double[] prevScores = Arrays.copyOf(getScores(), numScores());
        status = update(params);
        if (status != OptimiserStatus.NOT_HALTED)
            return status;
        incIterations();
        return postUpdate(params, prevScores, getScores());
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
        if (params.maxIterations() > 0 && numIterations() >= params.maxIterations())
            return OptimiserStatus.MAX_ITERATIONS_EXCEEDED;
        if (params.maxSubIterations() > 0 && numSubIterations() >= params.maxSubIterations())
            return OptimiserStatus.MAX_SUB_ITERATIONS_EXCEEDED;
        return OptimiserStatus.NOT_HALTED;
    }

    /**
     * Performs an update of the model parameters. If successful, this method is
     * responsible for updating the optimiser scores and number of
     * sub-iterations performed via {@link #setScores}() and
     * {@link #incSubIterations}(), respectively.
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
     * @param curScores
     *            - The optimisation and validation scores after the update.
     * @return The status of the optimiser after successfully performing an
     *         update.
     */
    protected OptimiserStatus postUpdate(OptimisationParams params, double[] prevScores, double[] curScores) {
        final double prevScore = prevScores[0];
        final double diffScore = params.optimisationDirection() * (curScores[0] - prevScore);
        if (diffScore <= 0)
            return OptimiserStatus.SCORE_NOT_IMPROVED;
        if (params.scoreTolerance() > 0 && diffScore < params.scoreTolerance())
            return OptimiserStatus.SCORE_CONVERGED;
        if (params.relativeScoreTolerance() > 0 && diffScore < Math.abs(prevScore) * params.relativeScoreTolerance())
            return OptimiserStatus.RELATIVE_SCORE_CONVERGED;
        return OptimiserStatus.NOT_HALTED;
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
