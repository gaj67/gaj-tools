package afl.classifier;

import java.util.Collection;

import afl.features.DenseFeatureVector;
import afl.features.FeatureMatrix;
import afl.features.FeatureVector;
import afl.features.SparseFeatureVector;

public class TwoClassLogisticClassifier implements SubsetClassifier {

   private static final double UNCERTAIN_LOWER = 0.5 - 1e-3;
   private static final double UNCERTAIN_UPPER = 0.5 + 1e-3;
   private FeatureVector lambda = null;
   private double stepSize = 0.1;

   public TwoClassLogisticClassifier() {}

   public TwoClassLogisticClassifier(int numFeatures) {
      lambda = new SparseFeatureVector(numFeatures);
   }

   public int numClasses() {
      return 2;
   }

   public double[] posteriors(FeatureVector features) {
      if (lambda.length() != features.length())
         throw new IllegalArgumentException(
               String.format("Expected length %d, got %d",
                     lambda.length(), features.length()));
      double[] probs = new double[2];
      probs[1] = 1 - (probs[0] = p0(features));
      return probs;
   }

   public double update(FeatureMatrix features, int[] classes) {
      final int F = features.numColumns();
      if (lambda == null)
         lambda = new SparseFeatureVector(F);
      else if (F != lambda.length())
         throw new IllegalArgumentException(
               String.format("Expected %d feature columns, got %d", lambda.length(), F));
      final int N = features.numRows();
      if (N != classes.length)
         throw new IllegalArgumentException(
               String.format("Expected %d class rows, got %d",
                     N, classes.length));
      FeatureVector gradient = new DenseFeatureVector(F);
      double score = 0.0;
      for (int i = 0; i < N; i++) {
         FeatureVector x = features.get(i);
         int c = classes[i];
         double p0 = p0(x);
         score += score(p0, c);
         gradient = gradient.add(scoreGradient(p0, x, c));
      }
      score /= N;
      gradient = gradient.scale(1.0 / N);
      return update(score, gradient);
   }

   public double update(FeatureMatrix features, int[] classes, Collection<Integer> subset) {
      final int F = features.numColumns();
      if (lambda == null)
         lambda = new SparseFeatureVector(F);
      else if (F != lambda.length())
         throw new IllegalArgumentException(
               String.format("Expected %d feature columns, got %d", lambda.length(), F));
      final int N = features.numRows();
      if (N != classes.length)
         throw new IllegalArgumentException(
               String.format("Expected %d class rows, got %d", N, classes.length));
      FeatureVector gradient = new DenseFeatureVector(F);
      double score = 0.0;
      final int num = subset.size();
      for (int i : subset) {
         FeatureVector x = features.get(i);
         int c = classes[i];
         double p0 = p0(x);
         score += score(p0, c);
         gradient = gradient.add(scoreGradient(p0, x, c));
      }
      score /= num;
      gradient = gradient.scale(1.0 / num);
      return update(score, gradient);
   }

   public double updateExcluding(FeatureMatrix features, int[] classes, Collection<Integer> leaveOut) {
      final int F = features.numColumns();
      if (lambda == null)
         lambda = new SparseFeatureVector(F);
      else if (F != lambda.length())
         throw new IllegalArgumentException(
               String.format("Expected %d feature columns, got %d", lambda.length(), F));
      final int N = features.numRows();
      if (N != classes.length)
         throw new IllegalArgumentException(
               String.format("Expected %d class rows, got %d", N, classes.length));
      FeatureVector gradient = new DenseFeatureVector(F);
      double score = 0.0;
      int num = 0;
      for (int i = 0; i < N; i++) {
         if (leaveOut.contains(i)) continue;
         num++;
         FeatureVector x = features.get(i);
         int c = classes[i];
         double p0 = p0(x);
         score += score(p0, c);
         gradient = gradient.add(scoreGradient(p0, x, c));
      }
      score /= num;
      gradient = gradient.scale(1.0 / num);
      return update(score, gradient);
   }

   /**
    * Use simple gradient ascent to maximise the score.
    * @param score - The score of the training set using the pre-updated model parameters.
    * @param gradient - The gradient of the training score with respect to the model parameters,
    * evaluated at their pre-updated values.
    * @return The pre-updated training score.
    */
   private double update(double score, FeatureVector gradient) {
      lambda = lambda.add(gradient.scale(stepSize));
      return score;
   }

   /*
    * Assumes a model of the form<br/>
    * P(win(x)|x,y,e) = h(x,e)/[h(x,e)+h(y,e)],<br/>
    * where x and y represent the two teams, and e
    * represents the match environment. <p>
    * Under the logistic model, we let<br/>
    * h(x,e)=exp(lambda.g(x,e)),<br/>
    * such that<br/>
    * P(win(x)|x,y,e) = 1/[1+exp(-lambda.f(x,y,e))],<br/>
    * where the overall feature vector is<br/>
    * f(x,y,e)=g(x,e)-g(y,e).
    */
   /**
    * @param features - The Fx1 feature vector, x.
    * @return The class-0 posterior probability
    * <br/>P(c=0|x) = 1 / [1 + e^{-lambda . x}].
    * <br/>This is designed so that high feature values in x
    * support class 0, and low feature values support class 1.
    */
   private double p0(FeatureVector features) {
      return 1.0 / (1.0 + Math.exp(-lambda.dot(features)));
   }

   /**
    * @param p0 - The class-0 posterior probability, P(0|x).
    * @param c - The true class index of the feature vector x.
    * @return The log of the class-posterior probability, log P(c|x).
    * <br/>As a special extension of the two-class case, if c > 1 then
    * the average of the class-0 and class-1 scores is returned,
    * namely [log P(0|x) + log P(1|x)] / 2.
    */
   private double score(double p0, int c) {
      if (c == 0)
         return Math.log(p0);
      else if (c == 1)
         return Math.log(1 - p0);
      else
         return 0.5 * (Math.log(p0) + Math.log(1 - p0));
   }

   /**
    * @param p0 - The class-0 posterior probability, P(0|x).
    * @param x - The Fx1 feature vector.
    * @param c - The true class index of the feature vector x.
    * @return The gradient of the log of the class-posterior probability
    * with respect to the classifier parameters, namely d [log P(c|x)] / d lambda.
    * <br/>As a special extension of the two-class case, if c > 1 then
    * the average of the class-0 and class-1 score gradients is returned,
    * namely {d [log P(0|x)] / d lambda + d [log P(1|x)] / d lambda} / 2.
    * <p/>
    * Note that d p0 / d lambda = p0 * p1 * x = > d [log p0] / d lambda = p1 * x.
    * <br/>Thus, d [log p1] / d lambda = 1/p1 * d (1-p0) / d lambda = -p0 * x.
    */
   private FeatureVector scoreGradient(double p0, FeatureVector x, int c) {
      double weight;
      if (c == 0)
         weight = 1 - p0; // = p1.
      else if (c == 1)
         weight = -p0;
      else
         weight = 0.5 - p0; // = [p1 - p0] / 2.
      return x.scale(weight);
   }

   /**
    * @param p0 - The class-0 posterior probability, P(0|x).
    * @return The class index of the (first) highest class-posterior
    * probability.
    */
   private int prediction(double p0) {
      return (p0 >= 0.5) ? 0 : 1;
   }

   public Stats statistics(FeatureVector features, int klass) {
      if (features.length() != lambda.length())
         throw new IllegalArgumentException(
               String.format("Expected %d features, got %d",
                             lambda.length(), features.length()));
      Stats stats = new Stats();
      stats.numData = 1;
      double p0 = p0(features);
      stats.meanScore = score(p0, klass);
      stats.meanError = errorRate(p0(features), klass);
      return stats;
   }

   public Stats statistics(FeatureMatrix features, int[] classes) {
      if (lambda.length() != features.numColumns())
         throw new IllegalArgumentException(
               String.format("Expected %d feature columns, got %d",
                     lambda.length(), features.numColumns()));
      if (features.numRows() != classes.length)
         throw new IllegalArgumentException(
               String.format("Expected %d class rows, got %d",
                     features.numRows(), classes.length));
      double sumScore = 0.0, sumError = 0.0;
      double sumSqScore = 0.0, sumSqError = 0.0;
      final int N = features.numRows();
      for (int i = 0; i < N; i++) {
         double score = score(p0(features.get(i)), classes[i]);
         sumScore += score;
         sumSqScore += score * score;
         double error = errorRate(p0(features.get(i)), classes[i]);
         sumError += error;
         sumSqError += error * error;
      }
      Stats stats = new Stats();
      stats.numData = N;
      stats.meanScore = sumScore / N;
      stats.varScore = sumSqScore / N - stats.meanScore * stats.meanScore;
      stats.meanError = sumError / N;
      stats.varError = sumSqError / N - stats.meanError * stats.meanError;
      return stats;
   }

   private double errorRate(double p0, int klass) {
      if (UNCERTAIN_LOWER < p0 && p0 < UNCERTAIN_UPPER) {
         // Too close to call - predict a draw.
         //return (klass == 2) ? 1 /* draw correctly predicted */ : 0.5 /* draw incorrectly predicted */;
         // XXX: The above formula is consistent with our scoring 1/2 point for a correct prediction for team1
         // and another 1/2 point for a correct prediction for team2, with draws being interpreted as both teams winning.
         // However, the formula biases uncertain cases when draws exist. Let d be the proportion of drawn matches.
         // Then the expected error-rate will be 1*d + 0.5*(1-d) = 0.5 + 0.5*d > 0.5 when d > 0.
         // So we use an unbiased score instead.
         return 0.5;
      } else {
         return (klass == 2) ? 0.5 /* draw not predicted */ : (prediction(p0) != klass) ? 1 : 0;
         // Note that the predicted error-rate here is 0.5*d + [1*(1-c) + 0*c]*(1-d) = 0.5 when
         // the prob. of correct prediction is c=0.5.
      }
   }

   public void resetParameters() {
      lambda = null;
   }

   public Stats statistics(FeatureMatrix features, int[] classes, Collection<Integer> subset) {
      if (lambda.length() != features.numColumns())
         throw new IllegalArgumentException(
               String.format("Expected %d feature columns, got %d",
                     lambda.length(), features.numColumns()));
      if (features.numRows() != classes.length)
         throw new IllegalArgumentException(
               String.format("Expected %d class rows, got %d",
                     features.numRows(), classes.length));
      double sumScore = 0.0, sumError = 0.0;
      double sumSqScore = 0.0, sumSqError = 0.0;
      final int N = subset.size();
      for (int i : subset) {
         double score = score(p0(features.get(i)), classes[i]);
         sumScore += score;
         sumSqScore += score * score;
         double error = errorRate(p0(features.get(i)), classes[i]);
         sumError += error;
         sumSqError += error * error;
      }
      Stats stats = new Stats();
      stats.numData = N;
      stats.meanScore = sumScore / N;
      stats.varScore = sumSqScore / N - stats.meanScore * stats.meanScore;
      stats.meanError = sumError / N;
      stats.varError = sumSqError / N - stats.meanError * stats.meanError;
      return stats;
   }

   public Stats statisticsExcluding(FeatureMatrix features, int[] classes, Collection<Integer> leaveOut) {
      if (lambda.length() != features.numColumns())
         throw new IllegalArgumentException(
               String.format("Expected %d feature columns, got %d",
                     lambda.length(), features.numColumns()));
      if (features.numRows() != classes.length)
         throw new IllegalArgumentException(
               String.format("Expected %d class rows, got %d",
                     features.numRows(), classes.length));
      double sumScore = 0.0, sumError = 0.0;
      double sumSqScore = 0.0, sumSqError = 0.0;
      final int N = features.numRows();
      int n = 0;
      for (int i = 0; i < N; i++) {
         if (leaveOut.contains(i)) continue;
         n++;
         double score = score(p0(features.get(i)), classes[i]);
         sumScore += score;
         sumSqScore += score * score;
         double error = errorRate(p0(features.get(i)), classes[i]);
         sumError += error;
         sumSqError += error * error;
      }
      Stats stats = new Stats();
      stats.numData = n;
      stats.meanScore = sumScore / n;
      stats.meanError = sumError / n;
      stats.varScore = sumSqScore / n - stats.meanScore * stats.meanScore;
      stats.varError = sumSqError / n - stats.meanError * stats.meanError;
      return stats;
   }

   public FeatureVector getParameters() {
      return lambda;
   }

   public void setParameters(FeatureVector params) {
      lambda = params;
   }

}
