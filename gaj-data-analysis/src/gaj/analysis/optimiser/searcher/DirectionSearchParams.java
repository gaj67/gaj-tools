package gaj.analysis.optimiser.searcher;

/**
 * Specifies the parameters for a search for a model parameters update
 * direction.
 */
public interface DirectionSearchParams {

    /**
     * Indicates the sign of the direction of score optimisation.
     * 
     * @return A positive (or negative) value corresponding to score
     *         maximisation (or minimisation).
     */
    int getDirectionSign();

    /**
     * Specifies the type of direction searcher to use.
     * 
     * @return The direction searcher type.
     */
    DirectionSearcherType getDirectionSearcherType();

}
