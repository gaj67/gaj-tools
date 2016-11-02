package gaj.analysis.classifier;

import gaj.analysis.curves.Quadratics;
import gaj.analysis.data.classifier.ClassifierScoreInfo;
import gaj.analysis.data.classifier.DataScorer;
import gaj.analysis.data.classifier.ParameterisedClassifier;
import gaj.analysis.data.vector.DataVector;

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
    protected QuadraticGradientAscentTrainer(ParameterisedClassifier classifier, DataScorer[] scorers) {
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
