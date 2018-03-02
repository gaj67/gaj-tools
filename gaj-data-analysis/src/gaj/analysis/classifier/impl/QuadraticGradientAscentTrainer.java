package gaj.analysis.classifier.impl;

import gaj.analysis.classifier.ClassifierScoreInfo;
import gaj.analysis.classifier.DataScorer;
import gaj.analysis.classifier.updated.OptimisableClassifier;
import gaj.analysis.curves.Quadratics;
import gaj.analysis.data.numeric.vector.DataVector;

/**
 * Implements a classifier trainer using
 * quadratic curve fitting to accelerate
 * gradient ascent, maximising the score(s)
 * on one or more fixed data sets.
 */
public class QuadraticGradientAscentTrainer extends GradientAscentTrainer {

    /**
     * Binds the training algorithm to the given classifier and scorers.
     * 
     * @param classifier - The classifier to be trained.
     * @param scorers - The data scorers to measure classifier performance.
     */
    protected QuadraticGradientAscentTrainer(OptimisableClassifier classifier, DataScorer[] scorers) {
        super(classifier, scorers);
    }

    @Override
    protected void recomputeStepSize(ClassifierScoreInfo newTrainingScore) {
        final DataVector g0 = getTrainingScore().getGradient();
        final DataVector g1 = newTrainingScore.getGradient();
        double s = Quadratics.quadraticOptimumScaling(g0, g1, direction);
        stepSize *= (s > 0 && s < 1) ? s : 0.5;
    }

    @Override
    protected void recomputeStepSizeAndDirection() {
        final DataVector g1 = getTrainingScore().getGradient();
        final DataVector g0 = getPrevTrainingScore().getGradient();
        double s = Quadratics.quadraticOptimumScaling(g0, g1, direction);
        stepSize *= Math.abs(s - 1);
        direction = g1;
    }

}
