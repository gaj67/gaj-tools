package afl.classifier;
import afl.features.FeatureMatrix;
import afl.features.FeaturesClassCollection;


public class Trainer {

   protected final Classifier classifier;
   private final int numFeatures;
   protected final FeatureMatrix trainingFeatures;
   protected final int[] trainingClasses;
   private final FeatureMatrix testingFeatures;
   private final int[] testingClasses;

   public Trainer(Classifier classifier, FeaturesClassCollection trainingData, FeaturesClassCollection testingData) {
      this.classifier = classifier;
      trainingFeatures = trainingData.features;
      trainingClasses = trainingData.classes.dense();
      this.numFeatures = trainingFeatures.numColumns();
      System.out.printf("Training data: N=%d, F=%d\n", trainingData.features.numRows(), numFeatures);
      if (testingData != null) {
         testingFeatures = testingData.features;
         testingClasses = testingData.classes.dense();
         int F2 = testingFeatures.numColumns();
         System.out.printf("Testing data: N=%d, F=%d\n", testingFeatures.numRows(), F2);
         if (F2 != numFeatures)
            throw new IllegalArgumentException("Number of features in testing data does not match training data");
      } else {
         testingFeatures = null;
         testingClasses = null;
      }
   }

   public Trainer(Classifier classifier, FeaturesClassCollection trainingData) {
      this(classifier, trainingData, null);
   }

   public void train(int maxIterations, double minDeltaScore, int reportQuantum) {
      double training_score = Double.NEGATIVE_INFINITY;
      Stats trainingStats = null, testingStats = null;
      if (testingClasses != null) {
         System.out.printf("Iter.   Training results       Testing results\n");
         System.out.printf("        Score   Prob. Errs     Score   Prob. Errs\n");
      } else {
         System.out.printf("Iter.   Training results\n");
         System.out.printf("        Score Prob. Errs\n");
      }
      int i;
      for (i = 0; i < maxIterations; i++) {
         if (testingClasses != null) {
            testingStats = classifier.statistics(testingFeatures, testingClasses);
         }
         trainingStats = classifier.statistics(trainingFeatures, trainingClasses);
         double old_training_score = training_score;
         training_score = classifier.update(trainingFeatures, trainingClasses);
         if (i % reportQuantum == 0) {
            if (testingClasses != null)
               System.out.printf("%4d %9.6f %6.3f %5.3f %9.6f %6.3f %5.3f\n", i,
                                 training_score, Math.exp(training_score),trainingStats.meanError,
                                 testingStats.meanScore, Math.exp(testingStats.meanScore), testingStats.meanError);
            else
               System.out.printf("%4d %9.6f %6.3f %5.3f\n", i,
                                 training_score, Math.exp(training_score), trainingStats.meanError);
         }
         if (training_score - old_training_score <= minDeltaScore) {
            System.out.printf("Convergence: diff. score=%9.6e\n", training_score - old_training_score);
            break;
         }
      }
      if (testingClasses != null) {
         testingStats = classifier.statistics(testingFeatures, testingClasses);
      }
      trainingStats = classifier.statistics(trainingFeatures, trainingClasses);
      if (testingClasses != null)
         System.out.printf("%4d %9.6f %6.3f %5.3f %9.6f %6.3f %5.3f\n", i,
                           trainingStats.meanScore, Math.exp(trainingStats.meanScore),trainingStats.meanError,
                           testingStats.meanScore, Math.exp(testingStats.meanScore), testingStats.meanError);
      else
         System.out.printf("%4d %9.6f %6.3f %5.3f\n", i,
               trainingStats.meanScore, Math.exp(trainingStats.meanScore),trainingStats.meanError);
   }

}
