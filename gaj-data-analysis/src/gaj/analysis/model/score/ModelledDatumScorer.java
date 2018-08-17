package gaj.analysis.model.score;

import gaj.analysis.model.AuxiliaryInfo;

/**
 * Provides a scorer bound to a data model that can score individual processed
 * cases.
 * 
 * <I> - The type of input data.
 * <O> - The type of output data.
 */
public interface ModelledDatumScorer<I, O> {

    /**
     * Scores the given case against the underlying model, and also computes any
     * associated information (e.g. the score gradient) if available.
     * 
     * @param dataCase
     *            - The input case to be scored.
     * @param dataOutput
     *            - The output obtained by processing the input data.
     * @param info
     *            - Optional objects either specifying auxiliary information for
     *            the model, or requesting auxiliary information be provided
     *            with the score.
     * @return The score of the case as processed by the model, optionally with
     *         any associated information.
     */
    ScoreInfo score(Datum<I> dataCase, O dataOutput, AuxiliaryInfo... info);

}
