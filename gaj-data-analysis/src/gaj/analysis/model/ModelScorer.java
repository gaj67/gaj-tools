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
     * @return The score of the model, plus any associated information.
     */
    ScoreInfo score(Model model);

    /**
     * Scores the given model, without computing any unnecessary side
     * information.
     * 
     * @param model
     *            - The model to be scored.
     * @return The numerical score of the model.
     */
    double scoreOnly(Model model);

}
