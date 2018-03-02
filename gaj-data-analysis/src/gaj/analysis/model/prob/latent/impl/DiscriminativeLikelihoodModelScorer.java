package gaj.analysis.model.prob.latent.impl;

import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.DataModel;
import gaj.analysis.model.prob.latent.ProbDataModel;
import gaj.analysis.model.score.DataCaseScorer;
import gaj.analysis.model.score.DataSource;
import gaj.analysis.model.score.impl.BaseDataModelScorer;

/**
 * Computes the discriminative score, S = sum_{i=1}^{N} w_i log p(y_i|x_i) /
 * sum_{i=1}^{N} w_i, obtained from a discriminative-capable model on a supplied
 * data set. Any unlabelled cases will be ignored.
 */
public class DiscriminativeLikelihoodModelScorer extends BaseDataModelScorer {

    protected DiscriminativeLikelihoodModelScorer(DataSource source) {
        super(source);
    }

    @Override
    protected DataCaseScorer getDataCaseScorer(DataModel model, AuxiliaryInfo info) {
        if (!(model instanceof ProbDataModel)) {
            throw new IllegalArgumentException("Require a probabilistic data model: " + model);
        }
        return new DiscriminativeLikelihoodCaseScorer((ProbDataModel) model, info);
    }

}
