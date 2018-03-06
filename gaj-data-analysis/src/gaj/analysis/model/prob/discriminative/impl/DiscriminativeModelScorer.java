package gaj.analysis.model.prob.discriminative.impl;

import gaj.analysis.data.DataObject;
import gaj.analysis.data.numeric.matrix.DataMatrix;
import gaj.analysis.data.numeric.matrix.impl.MatrixFactory;
import gaj.analysis.data.numeric.vector.DataVector;
import gaj.analysis.data.numeric.vector.impl.VectorFactory;
import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.prob.discriminative.DiscriminativeDataGradient;
import gaj.analysis.model.prob.discriminative.DiscriminativeDataObject;
import gaj.analysis.model.score.AuxiliaryScoreInfo;
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
 * data set.
 * <p/>
 * Note: For any unlabelled case (with y_i missing or negative), the case score
 * (and gradient, if computed) will either be ignored (by default) or be
 * posteriorly averaged across all classes (if specified).
 */
public class DiscriminativeModelScorer<I extends DataObject>
        extends DataSourceModelScorer<I, DiscriminativeDataObject>
{

    /**
     * Constructs a discriminative log-likelihood scorer using the supplied
     * source data.
     * <p/>
     * Note: For any unlabelled case, the case score (and gradient, if computed)
     * will either be ignored (by default) or be posteriorly averaged across all
     * classes (if requested).
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
            public ScoreInfo score(DataCase<I> dataCase, DiscriminativeDataObject dataOutput, AuxiliaryInfo... info) {
                final int labelIdx = (dataCase instanceof LabelledDataCase)
                        ? ((LabelledDataCase<I>) dataCase).getLabelIndex() : -1;
                if (labelIdx < 0) {
                    if (AuxiliaryScoreInfo.isUnlabelledAllowed(info)) {
                        return computeUnlabelledScore(dataOutput, dataCase.getWeight());
                    }
                    return ScoreInfo.NO_SCORE_INFO;
                }
                return computeLabelledScore(dataOutput, dataCase.getWeight(), labelIdx);
            }
        };
    }

    public static ScoreInfo computeLabelledScore(DiscriminativeDataObject probs, double weight, int labelIdx) {
        final double score = Math.log(probs.getPosteriorProbabilities().get(labelIdx));
        if (probs instanceof DiscriminativeDataGradient) {
            final DataVector gradient = ((DiscriminativeDataGradient) probs).getDiscriminativeGradients().getRow(labelIdx);
            return GradientScoreInfo.newScoreInfo(score, weight, gradient);
        }
        return ScoreInfo.newScoreInfo(score, weight);
    }

    public static ScoreInfo computeUnlabelledScore(DiscriminativeDataObject probs, double weight) {
        DataVector labelProbs = probs.getPosteriorProbabilities();
        DataVector logProbs = VectorFactory.apply(labelProbs, Math::log);
        final double score = VectorFactory.dot(labelProbs, logProbs);
        if (probs instanceof DiscriminativeDataGradient) {
            final DataMatrix gradients = ((DiscriminativeDataGradient) probs).getDiscriminativeGradients();
            final DataVector gradient = MatrixFactory.multiply(labelProbs, gradients);
            return GradientScoreInfo.newScoreInfo(score, weight, gradient);
        }
        return ScoreInfo.newScoreInfo(score, weight);
    }

}
