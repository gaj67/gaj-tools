package gaj.analysis.model.score.impl;

import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.Model;
import gaj.analysis.model.score.Datum;
import gaj.analysis.model.score.ModelledDatumScorer;
import gaj.analysis.model.score.DatumScorer;
import gaj.analysis.model.score.ModelScorer;
import gaj.analysis.model.score.DataSource;
import gaj.analysis.model.score.ScoreInfo;

/**
 * Provides the basis for implementing an additive scorer for a data model.
 * 
 * <I> - The input data type.
 * <O> - The output data type.
 */
public class DataSourceModelScorer<I, O> implements ModelScorer<I, O> {

    private final DataSource<I> source;
    private final ModelledDatumScorer<I, O> scorer;

    /**
     * Binds the scorer to a repeatable source of data.
     * 
     * @param source
     *            - The data source.
     */
    protected DataSourceModelScorer(DataSource<I> source, ModelledDatumScorer<I, O> scorer) {
        this.source = source;
        this.scorer = scorer;
    }

    protected DataSource<I> getSource() {
        return source;
    }

    @Override
    public ScoreInfo score(Model<I, O> model, AuxiliaryInfo... info) {
        DatumScorer<I> caseScorer = bindScorer(model, info);
        return source.stream().map(caseScorer::score).collect(new ScoreInfoCollector());
    }

    /**
     * Binds the given data model to a data case scorer.
     * 
     * @param model
     *            - The data model to be scored.
     * @param info
     *            - Optional auxiliary information.
     * @return A data case scorer.
     */
    protected DatumScorer<I> bindScorer(Model<I, O> model, AuxiliaryInfo... info) {
        return new DatumScorer<I>() {
            @Override
            public ScoreInfo score(Datum<I> dataCase) {
                O output = model.process(dataCase.getData(), info);
                return scorer.score(dataCase, output, info);
            }
        };
    }

}
