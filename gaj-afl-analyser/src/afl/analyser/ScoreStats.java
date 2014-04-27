package afl.analyser;

import gaj.afl.data.finalsiren.OldFinalSirenScraper;
import gaj.afl.data.match.MatchRecord;
import gaj.afl.data.match.OldScore;
import gaj.afl.data.match.Team;
import gaj.afl.data.match.TeamScores;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



public class ScoreStats {

   private static class InternalStats {
      private double sum = 0.0, sumsq = 0.0;
      private int num = 0;

      private void add(double value) {
         num++;
         sum += value;
         sumsq += value * value;
      }
   }

   public static class Stats {
      public double mean, variance;

      public Stats(double mean, double variance) {
         this.mean = mean;
         this.variance = variance;
      }

      public Stats(InternalStats stats) {
         this.mean = stats.sum / stats.num;
         if (stats.num > 1)
            this.variance = stats.sumsq / (stats.num - 1) - this.mean * this.mean; // Unbiased.
         else
            this.variance = stats.sumsq / stats.num - this.mean * this.mean; // Biased.
      }
   }

   public Stats goals, behinds, points;

   public ScoreStats(List<OldScore> scores) {
      InternalStats _goals = new InternalStats();
      InternalStats _behinds = new InternalStats();
      InternalStats _points = new InternalStats();
      for (OldScore score : scores) {
         _goals.add(score.goals);
         _behinds.add(score.behinds);
         _points.add(score.points);
      }
      goals = new Stats(_goals);
      behinds = new Stats(_behinds);
      points = new Stats(_points);
   }

   public String toString() {
      return String.format("goals(m%5.3f v%5.3f), behinds(m%5.3f v%5.3f), points(m%5.3f v%5.3f)",
                           goals.mean, goals.variance, behinds.mean, behinds.variance,
                           points.mean, points.variance);
   }

   public static void main(String[] args) throws IOException {
      Map<Team,List<OldScore>> records = new HashMap<Team,List<OldScore>>();
      File dir = new File("data/training/finalsiren/match");
      for (File file : dir.listFiles()) {
         addScores(records, OldFinalSirenScraper.scrapeFolder(file.getAbsoluteFile()));
      }
      List<OldScore> allScores = new ArrayList<OldScore>();
      for (Entry<Team, List<OldScore>> entry : records.entrySet()) {
         System.out.printf("%s: %s\n", entry.getKey(), new ScoreStats(entry.getValue()));
         allScores.addAll(entry.getValue());
      }
      System.out.printf("Overall: %s\n", new ScoreStats(allScores));
   }

   public static void addScores(Map<Team, List<OldScore>> records, List<MatchRecord> matches) {
      if (matches == null) return;
      for (MatchRecord rec : matches) {
         addScores(records, rec.homeTeam);
         addScores(records, rec.awayTeam);
      }
   }

   private static void addScores(Map<Team, List<OldScore>> records, TeamScores team) {
      List<OldScore> allScores = records.get(team.team);
      if (allScores == null) {
         allScores = new ArrayList<OldScore>();
         records.put(team.team, allScores);
      }
      OldScore prevScore = null;
      for (OldScore score : team.scores) {
         allScores.add(score.subtract(prevScore));
         prevScore = score;
      }
   }

}
