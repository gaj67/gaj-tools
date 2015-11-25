package afl.classifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import afl.features.FeatureVector;
import afl.features.FeaturesClassCollection;

public class SubsetTrainer extends Trainer {

    public SubsetTrainer(SubsetClassifier classifier, FeaturesClassCollection trainingData, FeaturesClassCollection testingData) {
        super(classifier, trainingData, testingData);
    }

    public SubsetTrainer(SubsetClassifier classifier, FeaturesClassCollection trainingData) {
        super(classifier, trainingData);
    }

    public void crossValidationLeaveOneOut(int maxIterations, double minDeltaScore) {
        System.out.printf("LeaveOneOut...\n");
        final int N = trainingFeatures.numRows();
        Stats[] multiStats = new Stats[N];
        Set<Integer> leaveOut = new HashSet<Integer>();
        for (int d = 0; d < N; d++) {
            leaveOut.add(d);
            classifier.resetParameters();
            trainExclusive(maxIterations, minDeltaScore, leaveOut);
            // Stats trainingStats = ((SubsetClassifier)classifier).statisticsExcluding(trainingFeatures, trainingClasses, leaveOut);
            Stats testingStats = ((SubsetClassifier) classifier).statistics(trainingFeatures, trainingClasses, leaveOut);
            multiStats[d] = testingStats;
            // System.out.printf("training: %9.6f [%5.3f], testing: %9.6f [%5.3f]\n",
            // trainingStats.meanScore, trainingStats.meanError, testingStats.meanScore, testingStats.meanError);
            leaveOut.remove(d);
        }
        System.out.printf("Micro Average: %s\n", Stats.microAverage(multiStats));
        Stats macroAverage = Stats.macroAverage(multiStats);
        System.out.printf("Macro Average: %s\n", macroAverage);
        double devMeanError = Math.sqrt(macroAverage.varError / macroAverage.numData);
        System.out.printf("Approx. confidence interval for mean-error: %5.3f -- %5.3f\n",
                macroAverage.meanError - 2 * devMeanError, macroAverage.meanError + 2 * devMeanError);
    }

    private int trainExclusive(int maxIterations, double minDeltaScore, Collection<Integer> leaveOut) {
        double training_score = Double.NEGATIVE_INFINITY;
        int i = 0;
        for (; i < maxIterations; i++) {
            double old_training_score = training_score;
            training_score = ((SubsetClassifier) classifier).updateExcluding(trainingFeatures, trainingClasses, leaveOut);
            if (training_score - old_training_score <= minDeltaScore)
                break;
        }
        return i;
    }

    private int trainInclusive(int maxIterations, double minDeltaScore, Collection<Integer> sample) {
        double training_score = Double.NEGATIVE_INFINITY;
        int i = 0;
        for (; i < maxIterations; i++) {
            double old_training_score = training_score;
            training_score = ((SubsetClassifier) classifier).update(trainingFeatures, trainingClasses, sample);
            if (training_score - old_training_score <= minDeltaScore)
                break;
        }
        return i;
    }

    private int trainAll(int maxIterations, double minDeltaScore) {
        double training_score = Double.NEGATIVE_INFINITY;
        int i = 0;
        for (; i < maxIterations; i++) {
            double old_training_score = training_score;
            training_score = classifier.update(trainingFeatures, trainingClasses);
            if (training_score - old_training_score <= minDeltaScore)
                break;
        }
        return i;
    }

    public void trainNFoldAverage(int numFolds, int maxIterations, double minDeltaScore) {
        System.out.printf("%d-fold cross-validation...\n", numFolds);
        FeatureVector sumParams = null;
        final int N = trainingClasses.length;
        Stats[] multiStats = new Stats[numFolds];
        for (int fold = 0; fold < numFolds; fold++) {
            Random generator = new Random();
            List<Integer> sample = new ArrayList<Integer>(N);
            for (int i = 0; i < N; i++)
                sample.add(generator.nextInt(N)); // Sample with replacement.
            classifier.resetParameters();
            trainInclusive(maxIterations, minDeltaScore, sample);
            Stats stats = ((SubsetClassifier) classifier).statistics(trainingFeatures, trainingClasses, sample);
            multiStats[fold] = stats;
            // System.out.printf("Fold %d: %s\n", fold+1, stats);
            FeatureVector params = classifier.getParameters();
            // printParams(params);
            if (sumParams == null)
                sumParams = params;
            else
                sumParams = sumParams.add(params);
        }
        System.out.printf("Micro Average: %s\n", Stats.microAverage(multiStats));
        System.out.printf("Macro Average: %s\n", Stats.macroAverage(multiStats));
        classifier.resetParameters();
        trainAll(maxIterations, minDeltaScore);
        Stats stats = classifier.statistics(trainingFeatures, trainingClasses);
        System.out.printf("Single Model: %s\n", stats);
        printParams(classifier.getParameters());
        classifier.setParameters(sumParams.scale(1.0 / numFolds));
        stats = classifier.statistics(trainingFeatures, trainingClasses);
        System.out.printf("Average Model: %s\n", stats);
        printParams(classifier.getParameters());
    }

    public void crossValidationNFold(int numFolds, double proportionTesting, int maxIterations, double minDeltaScore) {
        final int N = trainingClasses.length;
        int S = (int) (proportionTesting * N);
        if (S <= 0)
            S = 1;
        System.out.printf("%d-fold cross-validation, training size %d, testing size %d...\n", numFolds, N - S, S);
        FeatureVector sumParams = null;
        Stats[] multiStats = new Stats[numFolds];
        for (int fold = 0; fold < numFolds; fold++) {
            Random generator = new Random();
            List<Integer> train_sample = new ArrayList<Integer>(N - S);
            for (int i = S; i < N; i++)
                train_sample.add(generator.nextInt(N)); // Sample with replacement.
            classifier.resetParameters();
            trainInclusive(maxIterations, minDeltaScore, train_sample);
            Stats stats = ((SubsetClassifier) classifier).statisticsExcluding(trainingFeatures, trainingClasses, train_sample);
            multiStats[fold] = stats;
            // System.out.printf("Fold %d: %s\n", fold+1, stats);
            FeatureVector params = classifier.getParameters();
            // printParams(params);
            if (sumParams == null)
                sumParams = params;
            else
                sumParams = sumParams.add(params);
        }
        System.out.printf("Micro Average: %s\n", Stats.microAverage(multiStats));
        Stats macroAverage = Stats.macroAverage(multiStats);
        System.out.printf("Macro Average: %s\n", macroAverage);
        double devMeanError = Math.sqrt(macroAverage.varError/* /macroAverage.numData */);
        System.out.printf("Approx. confidence interval for mean-error: %5.3f -- %5.3f\n",
                macroAverage.meanError - 2 * devMeanError, macroAverage.meanError + 2 * devMeanError);
    }

    private static void printParams(FeatureVector params) {
        System.out.print("Parameters: [");
        for (int i = 0; i < params.length(); i++)
            System.out.printf(" %6.3f", params.get(i));
        System.out.print(" ]\n");
    }

}
