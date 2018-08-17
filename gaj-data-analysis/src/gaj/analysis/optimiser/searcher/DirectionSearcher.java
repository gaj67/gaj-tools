package gaj.analysis.optimiser.searcher;

import gaj.analysis.numeric.vector.SettableVector;

/**
 * Specifies a search algorithm to be used in conjunction with a bound
 * optimiser. Its purpose is to search for a feasible direction for a model
 * parameter update to improve the optimisation score.
 */
public interface DirectionSearcher {

    /**
     * Determines a feasible search direction by which to update the model
     * parameters.
     * 
     * @return The status of the search.
     */
    DirectionSearchStatus search(SettableVector direction);

}
