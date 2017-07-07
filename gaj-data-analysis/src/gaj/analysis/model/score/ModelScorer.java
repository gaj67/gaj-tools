package gaj.analysis.model.score;

import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.Model;

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
     * @param info
     *            - An object either specifying auxiliary information for the
     *            model, or requesting auxiliary information be provided with
     *            the score.
     * @return The score of the model, optionally with any associated
     *         information.
     */
    ScoreInfo score(Model model, AuxiliaryInfo info);

}
