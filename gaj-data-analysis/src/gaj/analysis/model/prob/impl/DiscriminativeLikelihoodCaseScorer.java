package gaj.analysis.model.prob.impl;

import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.DataObject;
import gaj.analysis.model.prob.DiscriminativeOutput;
import gaj.analysis.model.prob.ProbDataModel;
import gaj.analysis.model.prob.ProbModelType;
import gaj.analysis.model.score.DataCase;
import gaj.analysis.model.score.GradientScoreInfo;
import gaj.analysis.model.score.WeightedScoreInfo;
import gaj.analysis.model.score.impl.BaseDataCaseScorer;
import gaj.analysis.numeric.vector.DataVector;

/**
 * Computes the discriminative score, S_i = w_i log p(y_i|x_i), obtained from a
 * discriminative-capable model on a supplied data case. An unlabelled case will
 * be ignored (i.e. be assigned zero score and weight).
 */
public class DiscriminativeLikelihoodCaseScorer extends BaseDataCaseScorer {

    public DiscriminativeLikelihoodCaseScorer(ProbDataModel model, AuxiliaryInfo info) {
        super(model, info);
        if (model.getProbModelType() == ProbModelType.MARGINAL) {
            throw new IllegalArgumentException("Require a probabilistic data model capable of computing the discriminative likelihood: " + model);
        }
    }

    @Override
    protected WeightedScoreInfo score(DataCase dataCase, DataObject output, AuxiliaryInfo info) {
        if (dataCase.getWeight() == 0.0) {
            // Ignored case.
            return new SimpleScore(0, 0);
        }
        if (dataCase.getIndex() < 0) {
            // Unlabelled case.
            return new SimpleScore(0, 0);
        }
        if (!(output instanceof DiscriminativeOutput)) {
            throw new IllegalArgumentException("Require a discriminative output: " + output);
        }
        DataVector probs = ((DiscriminativeOutput) output).getPosteriorProbabilities();
        // TODO Compute the score gradient if requested.
        return new SimpleScore(Math.log(probs.get(dataCase.getIndex())), dataCase.getWeight());
    }

    // ========================================================================================
    private static class SimpleScore implements WeightedScoreInfo {

        private final double score;
        private final double weight;

        private SimpleScore(double score, double weight) {
            this.score = score;
            this.weight = weight;

        }

        @Override
        public double getScore() {
            return score;
        }

        @Override
        public double getWeight() {
            return weight;
        }

    }

    // ========================================================================================
    private static class GradientScore implements GradientScoreInfo {

        private final double score;
        private final double weight;
        private final DataVector gradient;

        private GradientScore(double score, double weight, DataVector gradient) {
            this.score = score;
            this.weight = weight;
            this.gradient = gradient;

        }

        @Override
        public double getScore() {
            return score;
        }

        @Override
        public double getWeight() {
            return weight;
        }

        @Override
        public DataVector getGradient() {
            return gradient;
        }

    }

}
