package gaj.analysis.model.score;

import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.AuxiliaryInfo.GradientAware;
import gaj.analysis.model.Model;

/**
 * Provides a way of scoring a data model.
 * 
 * <I> - The input data type.
 * <O> - The output data type.
 */
public interface ModelScorer<I, O> {

    /**
     * Scores the given model, and also computes any associated information
     * (e.g. the parameter score gradient) if available.
     * 
     * @param model
     *            - The model to be scored.
     * @param info
     *            - Optional objects either specifying auxiliary information for
     *            the model, or requesting auxiliary information be provided
     *            with the score.
     * @return The score of the model, optionally with any associated
     *         information.
     */
    ScoreInfo score(Model<I, O> model, AuxiliaryInfo... info);

    /**
     * Determines whether or not gradient information can and should be computed
     * as part of the score information, if possible.
     * 
     * @param info
     *            - Optional auxiliary information.
     * @return A value of true (or false) if gradient information is (or is not)
     *         requested to be computed.
     */
    default boolean scoreGradient(AuxiliaryInfo... info) {
        return this instanceof GradientAware && AuxiliaryInfo.isGradientAware(info);
    }

}
