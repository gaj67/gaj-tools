package gaj.analysis.model.score;

/**
 * Provides a scorer bound to a model that can score individual cases.
 * 
 * <I> - The type of data case.
 */
public interface DatumScorer<I> {

    /**
     * Scores the given case against the underlying model, and also computes any
     * associated information (e.g. the score gradient) if available.
     * 
     * @param dataCase
     *            - The case to be scored.
     * @return The score of the case as processed by the model, optionally with
     *         any associated information.
     */
    ScoreInfo score(Datum<I> dataCase);

}
