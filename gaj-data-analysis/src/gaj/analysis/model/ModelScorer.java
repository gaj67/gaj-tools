package gaj.analysis.model;

/**
 * Provides a way of scoring a model.
 */
public interface ModelScorer {

    /**
     * Scores the given model.
     * 
     * @param model
     *            - The model to be scored.
     * @return The score of the model.
     */
    ScoreInfo score(Model model);

}
