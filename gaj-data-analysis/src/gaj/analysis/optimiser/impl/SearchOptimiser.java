package gaj.analysis.optimiser.impl;

import gaj.analysis.model.ModelScorer;
import gaj.analysis.model.OptimisableModel;
import gaj.analysis.numeric.vector.SettableVector;
import gaj.analysis.numeric.vector.impl.VectorFactory;
import gaj.analysis.optimiser.DirectionSearchParams;
import gaj.analysis.optimiser.DirectionSearchStatus;
import gaj.analysis.optimiser.DirectionSearcher;
import gaj.analysis.optimiser.LineSearchParams;
import gaj.analysis.optimiser.LineSearchStatus;
import gaj.analysis.optimiser.LineSearcher;
import gaj.analysis.optimiser.OptimisationParams;
import gaj.analysis.optimiser.OptimisationResults;
import gaj.analysis.optimiser.OptimisationStatus;

/**
 * Implements a generic optimiser that uses variants of direction search and
 * line search to maximise/minimise the model score.
 */
public class SearchOptimiser extends IterativeOptimiser {

    /**
     * The direction search to use in the current round of optimisation.
     */
    private DirectionSearcher dirSearcher;

    /**
     * The line search algorithm currently in use for the current round of
     * optimisation.
     */
    private LineSearcher lineSearcher;

    /**
     * The next direction in which to update the model parameters.
     */
    private final SettableVector direction;

    /**
     * Binds the optimiser to the given model and scorers.
     * 
     * @param model
     *            - The model to be optimised.
     * @param scorers
     *            - The scorers to measure model performance.
     */
    protected SearchOptimiser(OptimisableModel model, ModelScorer[] scorers) {
        super(model, scorers);
        direction = VectorFactory.newVector(model.getParameters().size());
    }

    @Override
    public OptimisationResults optimise(OptimisationParams params) {
        DirectionSearchParams dirSearchParams = DirectionSearcherFactory.getDirectionSearchParams(params);
        dirSearcher = DirectionSearcherFactory.newDirectionSearcher(this, dirSearchParams);
        LineSearchParams lineSearchParams = LineSearcherFactory.getLineSearchParams(params);
        lineSearcher = LineSearcherFactory.newLineSearcher(this, lineSearchParams);
        return super.optimise(params);
    }

    @Override
    protected OptimisationStatus update(OptimisationParams params) {
        DirectionSearchStatus dsStatus = dirSearcher.search(direction);
        if (dsStatus != DirectionSearchStatus.AVAILABLE) return dsStatus.getOptimisationStatus();
        LineSearchStatus lsStatus = lineSearcher.search(direction);
        return lsStatus.getOptimisationStatus();
    }

}
