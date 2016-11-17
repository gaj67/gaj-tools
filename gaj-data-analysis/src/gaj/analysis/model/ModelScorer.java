package gaj.analysis.model;

/**
 * Provides a way of scoring a model.
 */
public interface ModelScorer {

    /**
     * Scores the given model, and also computes any associated information
     * (e.g. the parameter score gradient) if available.
     * 
     * @param model
     *            - The model to be scored.
     * @param includeAuxiliary
     *            - A flag indicating whether (true) or not (false) to include
     *            auxiliary information (e.g. gradient, Hessian, etc.) in the
     *            output.
     * @return The score of the model, optionally with any associated
     *         information.
     */
    ScoreInfo score(Model model, boolean includeAuxiliary);

}
