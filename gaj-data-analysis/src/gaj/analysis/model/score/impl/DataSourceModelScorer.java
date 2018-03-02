package gaj.analysis.model.score.impl;

import gaj.analysis.data.DataObject;
import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.DataModel;
import gaj.analysis.model.score.DataCase;
import gaj.analysis.model.score.DataInputScorer;
import gaj.analysis.model.score.DataModelScorer;
import gaj.analysis.model.score.DataOutputScorer;
import gaj.analysis.model.score.DataSource;
import gaj.analysis.model.score.ScoreInfo;

/**
 * Provides the basis for implementing an additive scorer for a data model.
 */
public class DataSourceModelScorer<I extends DataObject, O extends DataObject> 
    implements DataModelScorer<I, O> 
{

    private final DataSource<I> source;
    private final DataOutputScorer<I, O> scorer;

    /**
     * Binds the scorer to a repeatable source of data.
     * 
     * @param source
     *            - The data source.
     */
    protected DataSourceModelScorer(DataSource<I> source, DataOutputScorer<I, O> scorer) {
        this.source = source;
        this.scorer = scorer;
    }

    protected DataSource<I> getSource() {
        return source;
    }

    @Override
    public ScoreInfo score(DataModel<I, O> model, AuxiliaryInfo... info) {
        DataInputScorer<I> caseScorer = bindScorer(model, info);
        return source.stream().map(caseScorer::score).collect(new ScoreInfoCollector());
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
    protected DataInputScorer<I> bindScorer(DataModel<I, O> model, AuxiliaryInfo... info) {
        return new DataInputScorer<I>() {
            @Override
            public ScoreInfo score(DataCase<I> dataCase) {
                O output = model.process(dataCase.getData(), info);
                return scorer.score(dataCase, output);
            }
        };
    }

}
