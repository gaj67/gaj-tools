package gaj.analysis.model.impl;

import gaj.analysis.model.DataCase;
import gaj.analysis.model.DataCaseScorer;
import gaj.analysis.model.DataModel;
import gaj.analysis.model.DataOutput;
import gaj.analysis.model.ScoreInfo;
import gaj.analysis.model.WeightedScoreInfo;

/**
 * Provides a base implementation of a data case scorer, bound to a specific
 * data model.
 */
public abstract class BaseDataCaseScorer implements DataCaseScorer
{

    private final DataModel model;
    private final boolean includeAuxiliary;

    /**
     * Binds the data case scorer to the specified data model.
     * 
     * @param model
     *            - The data model used to process each case.
     * @param includeAuxiliary
     *            - A flag indicating whether (true) or not (false) to include
     *            auxiliary information with the {@link ScoreInfo}.
     */
    protected BaseDataCaseScorer(DataModel model, boolean includeAuxiliary) {
        this.model = model;
        this.includeAuxiliary = includeAuxiliary;
    }

    @Override
    public WeightedScoreInfo score(DataCase dataCase) {
        DataOutput output = model.process(dataCase.getData(), includeAuxiliary);
        return score(dataCase, output, includeAuxiliary);
    }

    /**
     * Scores the data case according to the data model output.
     * 
     * @param dataCase
     *            - The data case.
     * @param output
     *            - The model output for the data case.
     * @param includeAuxiliary
     *            - A flag indicating whether (true) or not (false) to include
     *            auxiliary information with the score.
     * @return
     */
    protected abstract WeightedScoreInfo score(DataCase dataCase, DataOutput output, boolean includeAuxiliary);

}
