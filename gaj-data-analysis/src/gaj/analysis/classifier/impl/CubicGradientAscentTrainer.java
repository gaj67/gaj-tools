package gaj.analysis.classifier.impl;

import gaj.analysis.classifier.ClassifierScoreInfo;
import gaj.analysis.classifier.DataScorer;
import gaj.analysis.classifier.updated.OptimisableClassifier;
import gaj.analysis.curves.Cubics;
import gaj.analysis.numeric.vector.DataVector;

/**
 * Implements a classifier trainer using
 * cubic curve fitting to accelerate
 * gradient ascent, maximising the score(s)
 * on one or more fixed data sets.
 */
public class CubicGradientAscentTrainer extends GradientAscentTrainer {

    /**
     * Binds the training algorithm to the given classifier and scorers.
     * 
     * @param classifier - The classifier to be trained.
     * @param scorers - The data scorers to measure classifier performance.
     */
    protected CubicGradientAscentTrainer(OptimisableClassifier classifier,
            DataScorer[] scorers) {
        super(classifier, scorers);
    }

    @Override
    protected void recomputeStepSize(ClassifierScoreInfo newTrainingScore) {
        final ClassifierScoreInfo ts = getTrainingScore();
        double s = Cubics.cubicMaximumScaling(
                ts.getScore(), ts.getGradient(),
                newTrainingScore.getScore(), newTrainingScore.getGradient(),
                stepSize, direction);
        if (s > 0 && s < stepSize)
            stepSize *= s;
        else
            stepSize *= 0.5;
    }

    @Override
    protected void recomputeStepSizeAndDirection() {
        final ClassifierScoreInfo curInfo = getTrainingScore();
        final DataVector g1 = curInfo.getGradient();
        final ClassifierScoreInfo prevInfo = getPrevTrainingScore();
        double s = Cubics.cubicMaximumScaling(
                prevInfo.getScore(), prevInfo.getGradient(),
                curInfo.getScore(), g1,
                stepSize, direction);
        stepSize *= Math.abs(s - 1);
        direction = g1;
    }

}
