package gaj.analysis.model.score.impl;

import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.DataModel;
import gaj.analysis.model.Model;
import gaj.analysis.model.score.DataCaseScorer;
import gaj.analysis.model.score.DataSource;
import gaj.analysis.model.score.ModelScorer;
import gaj.analysis.model.score.ScoreInfo;

/**
 * Provides the basis for implementing an additive scorer for a data model.
 */
public abstract class BaseDataModelScorer implements ModelScorer {

    private final DataSource source;

    /**
     * Binds the scorer to a repeatable source of data.
     * 
     * @param source
     *            - The data source.
     */
    protected BaseDataModelScorer(DataSource source) {
        this.source = source;
    }

    protected DataSource getSource() {
        return source;
    }

    @Override
    public ScoreInfo score(Model model, AuxiliaryInfo info) {
        if (!(model instanceof DataModel)) {
            throw new IllegalArgumentException("Not a data model: " + model);
        }
        DataCaseScorer caseScorer = getDataCaseScorer((DataModel) model, info);
        return source.stream().map(caseScorer::score).collect(new WeightedScoreInfoCollector());
    }

    /**
     * Binds the given data model to a data case scorer.
     * 
     * @param model
     *            - The data model to be scored.
     * @param info
     *            - An object indicating whether or not to include auxiliary
     *            information (e.g. gradient, Hessian, etc.) in the score
     *            output.
     * @return A data case scorer.
     */
    protected abstract DataCaseScorer getDataCaseScorer(DataModel model, AuxiliaryInfo info);

}
