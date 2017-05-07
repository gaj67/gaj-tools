package gaj.analysis.model.impl;

import gaj.analysis.model.DataCaseScorer;
import gaj.analysis.model.DataModel;
import gaj.analysis.model.DataSource;
import gaj.analysis.model.Model;
import gaj.analysis.model.ModelScorer;
import gaj.analysis.model.ScoreInfo;

/**
 * Provides the basis for implementing a scorer for a data model.
 */
public abstract class DataModelScorer implements ModelScorer {

    private final DataSource source;

    /**
     * Binds the scorer to a repeatable source of data.
     * 
     * @param source
     *            - The data source.
     */
    protected DataModelScorer(DataSource source) {
        this.source = source;
    }

    protected DataSource getSource() {
        return source;
    }

    @Override
    public ScoreInfo score(Model model, boolean includeAuxiliary) {
        if (!(model instanceof DataModel)) {
            throw new IllegalArgumentException("Not a data model: " + model);
        }
        DataCaseScorer caseScorer = getDataCaseScorer((DataModel) model, includeAuxiliary);
        return source.stream().map(caseScorer::score).collect(new WeightedScoreInfoCollector());
    }

    /**
     * Binds the given data model to a data case scorer.
     * 
     * @param model
     *            - The data model to be scored.
     * @param includeAuxiliary
     *            - A flag indicating whether (true) or not (false) to include
     *            auxiliary information (e.g. gradient, Hessian, etc.) in the
     *            score output.
     * @return A data case scorer.
     */
    protected abstract DataCaseScorer getDataCaseScorer(DataModel model, boolean includeAuxiliary);

}
