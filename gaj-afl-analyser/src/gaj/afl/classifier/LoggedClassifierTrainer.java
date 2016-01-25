package gaj.afl.classifier;

import gaj.analysis.classifier.AccelerationType;
import gaj.analysis.classifier.AccuracyScorer;
import gaj.analysis.classifier.ClassifierFactory;
import gaj.analysis.classifier.LogProbScorer;
import gaj.data.classifier.DataScorer;
import gaj.data.classifier.GoldData;
import gaj.data.classifier.ScoredTrainer;
import gaj.data.classifier.TrainableClassifier;
import gaj.data.classifier.TrainingControl;
import gaj.data.classifier.TrainingState;
import gaj.data.classifier.TrainingSummary;
import gaj.data.vector.DataVector;
import gaj.impl.vector.VectorFactory;

/**
 * Offers a simple mechanism for training a given classifier on given data, using gradient ascent.
 * The iteration scores can optionally be traced for each major iteration.
 */
public class LoggedClassifierTrainer {

    private final TrainableClassifier classifier;
    private final GoldData trainingData;
    private final GoldData[] testingData;

    public LoggedClassifierTrainer(TrainableClassifier classifier, GoldData trainingData, GoldData... testingData) {
        this.classifier = classifier;
        this.trainingData = trainingData;
        this.testingData = testingData;

    }

    public static LoggedClassifierTrainer getTrainer(GoldData trainingData, GoldData testingData, AccelerationType type) {
        return new LoggedClassifierTrainer(
                ClassifierFactory.newTrainableClassifier(trainingData.numClasses(), trainingData.numFeatures(), type),
                trainingData, testingData);
    }

    public static LoggedClassifierTrainer getTrainer(GoldData trainingData, GoldData... testingData) {
        return new LoggedClassifierTrainer(
                ClassifierFactory.newTrainableClassifier(trainingData.numClasses(), trainingData.numFeatures(), AccelerationType.Quadratic),
                trainingData, testingData);
    }

    public void train(int traceIterations) {
        System.out.printf("Showing score trace: %s%n",
                (traceIterations > 0) ? ("every " + traceIterations + " iterations") : "no");
        TrainingControl control = getControl(traceIterations);
        train(classifier.getTrainer(getScorers()), control);
    }

    protected DataScorer[] getScorers() {
        DataScorer[] scorers = new DataScorer[2 * (1 + testingData.length)];
        int i = 0;
        scorers[i++] = new LogProbScorer(trainingData);
        scorers[i++] = new AccuracyScorer(trainingData, 1e-3);
        for (GoldData testData : testingData) {
            scorers[i++] = new LogProbScorer(testData);
            scorers[i++] = new AccuracyScorer(testData, 1e-3);
        }
        return scorers;
    }

    protected void train(ScoredTrainer trainer, TrainingControl control) {
        long start = System.currentTimeMillis();
        System.out.println("Iteration, scores, prameters");
        printScores(trainer.numIterations(), trainer.getScores(), classifier.getParameters());
        TrainingState state = TrainingState.NOT_HALTED;
        while (state != TrainingState.SCORE_CONVERGED) {
            TrainingSummary summary = trainer.train(control);
            printScores(trainer.numIterations(), trainer.getScores(), classifier.getParameters());
            state = summary.getTrainingState();
            if (state != TrainingState.MAX_ITERATIONS_EXCEEDED) {
                // Training ceased earlier than expected, e.g. due to convergence.
                break;
            }
        }
        System.out.printf("Final state=%s%n", state);
        long end = System.currentTimeMillis();
        double time = 1e-3 * (end - start);
        System.out.printf("#iterations=%d, time=%4.2f seconds (%4.2f ms/iter)%n",
                trainer.numIterations(), time, 1e3 * time / trainer.numIterations());
        VectorFactory.display("Final classifier parameters =", classifier.getParameters(), "\n");
    }

    private static void printScores(int numIterations, double[] scores, DataVector parameters) {
        System.out.printf("%d, [", numIterations);
        for (double score : scores)
            System.out.printf(" %f", score);
        System.out.print(" ], ");
        VectorFactory.display(parameters);
        System.out.println();
    }

    protected static TrainingControl getControl(final int maxIterations) {
        return new TrainingControl() {
            @Override
            public double scoreTolerance() {
                return 1e-14;
            }

            @Override
            public int maxIterations() {
                return maxIterations;
            }

            @Override
            public int maxSubIterations() {
                return 10;
            }

            @Override
            public double gradientTolerance() {
                return 0;
            }

            @Override
            public double relativeScoreTolerance() {
                return 0;
            }
        };
    }

}
