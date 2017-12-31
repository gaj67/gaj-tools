package gaj.analysis.model.score.impl;

import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.DataModel;
import gaj.analysis.model.DataObject;
import gaj.analysis.model.score.DataCase;
import gaj.analysis.model.score.DataCaseScorer;
import gaj.analysis.model.score.ScoreInfo;
import gaj.analysis.model.score.WeightedScoreInfo;

/**
 * Provides a base implementation of a data case scorer, bound to a specific
 * data model.
 */
public abstract class BaseDataCaseScorer implements DataCaseScorer
{

    private final DataModel model;
    private final AuxiliaryInfo info;

    /**
     * Binds the data case scorer to the specified data model.
     * 
     * @param model
     *            - The data model used to process each case.
     * @param info
     *            - An object indicating whether or not to include auxiliary
     *            information with the {@link ScoreInfo}.
     */
    protected BaseDataCaseScorer(DataModel model, AuxiliaryInfo info) {
        this.model = model;
        this.info = info;
    }

    @Override
    public WeightedScoreInfo score(DataCase dataCase) {
        DataObject output = model.process(dataCase.getData(), info);
        return score(dataCase, output, info);
    }

    /**
     * Scores the data case according to the data model output.
     * 
     * @param dataCase
     *            - The data case.
     * @param output
     *            - The model output for the data case.
     * @param info
     *            - An object either specifying auxiliary information for the
     *            model, or requesting auxiliary information be provided with
     *            the score.
     * @return The weighted score of the data case.
     */
    protected abstract WeightedScoreInfo score(DataCase dataCase, DataObject output, AuxiliaryInfo info);

}
