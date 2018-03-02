package gaj.analysis.model.prob.discriminative.impl;

import gaj.analysis.data.DataObject;
import gaj.analysis.data.numeric.vector.DataVector;
import gaj.analysis.model.prob.discriminative.DiscriminativeDataGradient;
import gaj.analysis.model.prob.discriminative.DiscriminativeDataObject;
import gaj.analysis.model.score.DataCase;
import gaj.analysis.model.score.DataOutputScorer;
import gaj.analysis.model.score.DataSource;
import gaj.analysis.model.score.GradientScoreInfo;
import gaj.analysis.model.score.LabelledDataCase;
import gaj.analysis.model.score.ScoreInfo;
import gaj.analysis.model.score.impl.DataSourceModelScorer;

/**
 * Computes the discriminative score, S = sum_{i=1}^{N} w_i log p(y_i|x_i) /
 * sum_{i=1}^{N} w_i, obtained from a discriminative-capable model on a supplied
 * data set. Any unlabelled cases will be ignored.
 */
public class DiscriminativeModelScorer<I extends DataObject>
        extends DataSourceModelScorer<I, DiscriminativeDataObject>
{

    /**
     * Constructs a discriminative log-likelihood scorer using the supplied
     * source data. Note that unlabelled cases will not be scored.
     * 
     * @param source
     *            - The source of data cases.
     */
    public DiscriminativeModelScorer(DataSource<I> source) {
        super(source, getScorer());
    }

    private static <I extends DataObject> DataOutputScorer<I, DiscriminativeDataObject> getScorer() {
        return new DataOutputScorer<I, DiscriminativeDataObject>() {
            @Override
            public ScoreInfo score(DataCase<I> dataCase, DiscriminativeDataObject dataOutput) {
                final int labelIdx = (dataCase instanceof LabelledDataCase)
                        ? ((LabelledDataCase<I>) dataCase).getLabelIndex() : -1;
                if (labelIdx < 0) return ScoreInfo.NO_SCORE_INFO;
                final double weight = dataCase.getWeight();
                // TODO Handle a prob. of zero.
                final double score = Math.log(dataOutput.getPosteriorProbabilities().get(labelIdx));
                if (dataOutput instanceof DiscriminativeDataGradient) {
                    final DataVector gradient = ((DiscriminativeDataGradient) dataOutput)
                            .getDiscriminativeGradients().getRow(labelIdx);
                    return GradientScoreInfo.newScoreInfo(score, weight, gradient);
                }
                return ScoreInfo.newScoreInfo(score, weight);
            }
        };
    }

}
