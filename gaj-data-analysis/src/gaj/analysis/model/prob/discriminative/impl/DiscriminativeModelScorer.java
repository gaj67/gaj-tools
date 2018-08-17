package gaj.analysis.model.prob.discriminative.impl;

import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.GradientAware;
import gaj.analysis.model.prob.discriminative.DiscriminativeProbs;
import gaj.analysis.model.prob.discriminative.DiscriminativeProbsGradient;
import gaj.analysis.model.score.DataSource;
import gaj.analysis.model.score.Datum;
import gaj.analysis.model.score.GradientScoreInfo;
import gaj.analysis.model.score.LabelledDatum;
import gaj.analysis.model.score.ModelledDatumScorer;
import gaj.analysis.model.score.ScoreInfo;
import gaj.analysis.model.score.ScorerInfo;
import gaj.analysis.model.score.impl.DataSourceModelScorer;
import gaj.analysis.numeric.matrix.DataMatrix;
import gaj.analysis.numeric.matrix.impl.MatrixFactory;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.impl.VectorFactory;

/**
 * Computes the discriminative score, S = sum_{i=1}^{N} w_i log p(y_i|x_i) /
 * sum_{i=1}^{N} w_i, obtained from a discriminative-capable model on a supplied
 * data set.
 * <p/>
 * Note: For any unlabelled case (with y_i missing or negative), the case score
 * (and gradient, if computed) will either be ignored (by default) or be
 * posteriorly averaged across all classes (if specified).
 */
public class DiscriminativeModelScorer<I> extends DataSourceModelScorer<I, DiscriminativeProbs>
        implements GradientAware
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

    private static <I> ModelledDatumScorer<I, DiscriminativeProbs> getScorer() {
        return new ModelledDatumScorer<I, DiscriminativeProbs>() {
            @Override
            public ScoreInfo score(Datum<I> dataCase, DiscriminativeProbs dataOutput, AuxiliaryInfo... info) {
                final int labelIdx = (dataCase instanceof LabelledDatum) ? ((LabelledDatum<I>) dataCase).getLabelIndex()
                        : -1;
                if (labelIdx < 0) {
                    if (ScorerInfo.scoreUnlabelled(info)) {
                        return computeUnlabelledScore(dataOutput, dataCase.getWeight());
                    }
                    return ScoreInfo.NO_SCORE_INFO;
                }
                return computeLabelledScore(dataOutput, dataCase.getWeight(), labelIdx);
            }
        };
    }

    public static ScoreInfo computeLabelledScore(DiscriminativeProbs probs, double weight, int labelIdx) {
        final double score = Math.log(probs.getPosteriorProbabilities().get(labelIdx));
        if (probs instanceof DiscriminativeProbsGradient) {
            final DataVector gradient = ((DiscriminativeProbsGradient) probs).getDiscriminativeGradients().getRow(labelIdx);
            return GradientScoreInfo.newScoreInfo(score, weight, gradient);
        }
        return ScoreInfo.newScoreInfo(score, weight);
    }

    public static ScoreInfo computeUnlabelledScore(DiscriminativeProbs probs, double weight) {
        DataVector labelProbs = probs.getPosteriorProbabilities();
        DataVector logProbs = VectorFactory.apply(labelProbs, Math::log);
        final double score = VectorFactory.dot(labelProbs, logProbs);
        if (probs instanceof DiscriminativeProbsGradient) {
            final DataMatrix gradients = ((DiscriminativeProbsGradient) probs).getDiscriminativeGradients();
            final DataVector gradient = MatrixFactory.multiply(labelProbs, gradients);
            return GradientScoreInfo.newScoreInfo(score, weight, gradient);
        }
        return ScoreInfo.newScoreInfo(score, weight);
    }

}
