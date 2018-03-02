package gaj.analysis.model.score;

import gaj.analysis.data.DataObject;
import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.DataModel;

/**
 * Provides a way of scoring a data processing model.
 */
public interface DataModelScorer<I extends DataObject, O extends DataObject> {

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
    ScoreInfo score(DataModel<I, O> model, AuxiliaryInfo... info);

}
