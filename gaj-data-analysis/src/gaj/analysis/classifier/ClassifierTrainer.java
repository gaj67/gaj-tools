package gaj.analysis.classifier;

import gaj.data.classifier.DataScorer;
import gaj.data.classifier.ParameterisedClassifier;
import gaj.data.classifier.ScoredTrainer;
import gaj.data.classifier.TrainingControl;
import gaj.data.classifier.TrainingSummary;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * A base class for implementing a trainer for
 * a trainable classifier. The trainer is initialised
 * once, but may be repeatedly used (on the same data) by instantiating
 * the specified training algorithm class.
 */
public class ClassifierTrainer implements ScoredTrainer {

    private final TrainingAlgorithm trainer;
    /** Current, total number of training iterations performed. */
    private int numIterations = 0;

    /**
     * Binds the classifier and scorers to the training algorithm.
     * 
     * @param classifier - The classifier to be trained.
     * @param scorers - The scorers to be evaluated. The first scorer represents the training data,
     * and subsequent scorers, if any, represent testing data.
     * @param algo - The training algorithm.
     */
    protected ClassifierTrainer(ParameterisedClassifier classifier, DataScorer[] scorers, Class<? extends TrainingAlgorithm> algo) {
        try {
            Constructor<? extends TrainingAlgorithm> constructor = algo.getDeclaredConstructor(ParameterisedClassifier.class, DataScorer[].class);
            trainer = constructor.newInstance(classifier, scorers);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int numIterations() {
        return numIterations;
    }

    @Override
    public double[] getScores() {
        double[] scores = trainer.getScores();
        return Arrays.copyOf(scores, scores.length);
    }

    @Override
    public TrainingSummary train(TrainingControl control) {
        TrainingSummary summary = trainer.train(control);
        numIterations += summary.numIterations();
        return summary;
    }

}
