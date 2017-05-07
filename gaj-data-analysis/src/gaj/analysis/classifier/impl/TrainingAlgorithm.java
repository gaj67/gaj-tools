package gaj.analysis.classifier.impl;

import gaj.analysis.classifier.ClassifierScoreInfo;
import gaj.analysis.classifier.DataScorer;
import gaj.analysis.classifier.TrainingControl;
import gaj.analysis.classifier.TrainingSummary;
import gaj.analysis.classifier.updated.OptimisableClassifier;

/**
 * Specifies the training algorithm used to train a parameterised classifier on
 * gold-standard data.
 */
public abstract class TrainingAlgorithm {

    // **********************************************************
    // Instantiation interface.

    /** The classifier set by {@link #bindArguments}(). */
    protected final OptimisableClassifier classifier;
    /** The training and testing scorers set by {@link #bindArguments}(). */
    protected final DataScorer[] scorers;
    /** Indicates the number of training and testing scores. */
    private int numScores;
    /** Current training and testing scores. */
    private double[] scores;
    /** Current training score and any other information. */
    private ClassifierScoreInfo trainingScore;

    /**
     * Binds the training algorithm to the given classifier and scorers.
     * 
     * @param classifier - The classifier to be trained.
     * @param scorers - The data scorers to measure classifier performance.
     */
    protected TrainingAlgorithm(OptimisableClassifier classifier, DataScorer[] scorers) {
        this.classifier = classifier;
        this.scorers = scorers;
        numScores = scorers.length;
        scores = new double[numScores];
        trainingScore = computeScores(scores);
    }

    /**
     * Indicates the number of training and testing scores.
     * 
     * @return The length of the scores array.
     */
    protected int numScores() {
        return numScores;
    }

    /**
     * Computes the current training and testing scores of the classifier on the gold-standard
     * data.
     * 
     * @param scores - The array of training and testing scores.
     * @return The training score information.
     */
    protected ClassifierScoreInfo computeScores(double[] scores) {
        computeTestingScores(scores);
        return computeTrainingScore(scores);
    }

    /**
     * Computes the current training score of the classifier on the gold-standard
     * data.
     * 
     * @param scores - The array of training and testing scores.
     * @return The training score information.
     */
    protected ClassifierScoreInfo computeTrainingScore(double[] scores) {
        ClassifierScoreInfo info = scorers[0].getClassifierScoreInfo(classifier);
        scores[0] = info.getScore();
        return info;
    }

    /**
     * Computes the current testing scores of the classifier on the gold-standard
     * data.
     * 
     * @param scores - The array of training and testing scores.
     */
    protected void computeTestingScores(double[] scores) {
        for (int i = 1; i < scorers.length; i++)
            scores[i] = scorers[i].getClassifierScore(classifier);
    }

    // **********************************************************
    // Training-specific interface. The information generated is only valid
    // for the current training run.

    /** Current number of training iterations performed. */
    private int numIterations = 0;

    /**
     * Performs classifier training, and updates the scores via {@link #setScores}() and the number of iterations via {@link #incIterations}().
     * 
     * @param control - The settings to control termination of the training process.
     * @return A summary of the training iterations performed.
     */
    protected abstract TrainingSummary train(TrainingControl control);

    /**
     * Obtains the current number of training iterations performed.
     * 
     * @return The number of iterations.
     */
    protected int numIterations() {
        return numIterations;
    }

    /**
     * Increments the number of training iterations performed.
     */
    protected void incIterations() {
        numIterations++;
    }

    /**
     * Increments the number of training iterations performed by the specified amount.
     * 
     * @param numIterations - The number of additional iterations performed.
     */
    protected void incIterations(int numIterations) {
        this.numIterations += numIterations;
    }

    /**
     * Resets the number of iterations for a new training run.
     */
    protected void resetIterations() {
        numIterations = 0;
    }

    /**
     * Specifies the classifier training and testing scores, and other
     * training information.
     * 
     * @param trainingScore - The training score information.
     * @param scores - The training and testing scores.
     */
    protected void setScores(ClassifierScoreInfo trainingScore, double[] scores) {
        this.trainingScore = trainingScore;
        this.scores = scores;
    }

    /**
     * Obtains the current classifier training score information.
     * 
     * @return The training score information.
     */
    protected ClassifierScoreInfo getTrainingScore() {
        return trainingScore;
    }

    /**
     * Obtains the current classifier training and testing scores.
     * 
     * @return The array of scores.
     */
    protected double[] getScores() {
        return scores;
    }

}
