package afl.analyser;
import gaj.afl.data.core.MatchRecord;
import gaj.afl.data.finalsiren.OldFinalSirenScraper;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import afl.classifier.SubsetClassifier;
import afl.classifier.SubsetTrainer;
import afl.classifier.Trainer;
import afl.classifier.TwoClassLogisticClassifier;
import afl.features.FeatureVector;
import afl.features.FeaturesClassCollection;
import afl.features.SparseFeatureVector;
import afl.features.derived.StatisticalFeatures;


public class Train {

   public static void main(String[] args) throws IOException {
      Set<String> deduplicate = new HashSet<String>();
      FeaturesClassCollection trainingData = new FeaturesClassCollection();
      File path = new File("data/finalsiren/match");
	List<MatchRecord> records = OldFinalSirenScraper.scrapeFolder(path, 2008, 2009);
      for (MatchRecord record : records) {
         //System.out.printf("%s %s %s %d\n", record.homeTeam.team, record.homeTeamResult.toExternal(), record.awayTeam.team, record.homeTeamResult.getClassification());
         String label = String.format("%d_%d_%s", record.homeTeam.team.ordinal(), record.awayTeam.team.ordinal(), record.date.toString());
         if (deduplicate.contains(label)) continue;
         deduplicate.add(label);
         FeatureVector features = getFeatures(record);
         int klass = record.homeTeamResult.getClassification();
         trainingData.add(features, klass);
         //trainingData.add(features.scale(-1), (klass == 2) ? 2 : 1 - klass); // Add reversal to scramble fixed home-team advantage.
      }
      FeaturesClassCollection testingData = new FeaturesClassCollection();
      records = OldFinalSirenScraper.scrapeFolder(path, 2010);
      for (MatchRecord record : records) {
         //System.out.printf("%s %s %s %d\n", record.homeTeam.team, record.homeTeamResult.toExternal(), record.awayTeam.team, record.homeTeamResult.getClassification());
         String label = String.format("%d_%d_%s", record.homeTeam.team.ordinal(), record.awayTeam.team.ordinal(), record.date.toString());
         if (deduplicate.contains(label)) continue;
         deduplicate.add(label);
         testingData.add(getFeatures(record), record.homeTeamResult.getClassification());
      }
      int F = trainingData.features.numColumns();
      SubsetClassifier classifier = new TwoClassLogisticClassifier(F);
      final int maxIterations = 10000; // Number of iterations.
      final int reportQuantum = 1000; // Reporting period.
      final double minDeltaScore = 1e-7;
      Trainer trainer = new SubsetTrainer(classifier, trainingData, testingData);
      trainer.train(maxIterations, minDeltaScore, reportQuantum);
      FeatureVector params = classifier.getParameters();
      printParams(params);
      classifier.resetParameters();
      SubsetTrainer strainer = new SubsetTrainer(classifier, trainingData);
      strainer.crossValidationLeaveOneOut(maxIterations, minDeltaScore);
      final int numFolds = 100;
      //strainer.trainNFoldAverage(numFolds, maxIterations, minDeltaScore);
      strainer.crossValidationNFold(numFolds, 0.1, maxIterations, minDeltaScore);
   }

   private static void printParams(FeatureVector params) {
      System.out.print("Parameters: [");
      for (int i = 0; i < params.length(); i++)
         System.out.printf(" %6.3f", params.get(i));
      System.out.print(" ]\n");
   }

   private static FeatureVector getFeatures(MatchRecord match) {
      /*
      TeamFeatures hFeatures = new TeamFeatures(match.homeTeam.team);
      TeamFeatures aFeatures = new TeamFeatures(match.awayTeam.team);
      EnvironmentFeatures env = new EnvironmentFeatures(match.date, match.location);
      TeamEnvironmentFeatures heFeatures = new TeamEnvironmentFeatures(hFeatures, env);
      TeamEnvironmentFeatures aeFeatures = new TeamEnvironmentFeatures(aFeatures, env);
      return new SparseFeatureVector(heFeatures.features.subtract(aeFeatures.features));
      */
      /*
      FeatureVector constant = SparseFeatureVector.oneOfN(1, 0);
      return new SparseFeatureVector(constant, hFeatures.features, aFeatures.features, env.features);
      */

      /*
      // Give home-team advantage to specified home team.
      return SparseFeatureVector.oneOfN(1, 0);
      */
      // Give home-team advantage based upon team and ground.
      double homeAdv = StatisticalFeatures.getHomeAdvantage(match.homeTeam.team, match.location);
      double awayAdv = StatisticalFeatures.getHomeAdvantage(match.awayTeam.team, match.location);
      FeatureVector advantage = new SparseFeatureVector(new double[]{homeAdv-awayAdv});
      /*
      FeatureVector hFeatures = SparseFeatureVector.oneOfN(Team.values().length, match.homeTeam.team.ordinal());
      FeatureVector aFeatures = SparseFeatureVector.oneOfN(Team.values().length, match.awayTeam.team.ordinal());
      FeatureVector teamDiff = hFeatures.subtract(aFeatures);
      return new SparseFeatureVector(advantage, teamDiff);
      return new SparseFeatureVector(advantage);
      */
      return advantage;
   }

}
