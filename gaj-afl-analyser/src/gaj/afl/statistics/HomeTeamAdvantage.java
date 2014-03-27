package gaj.afl.statistics;

import gaj.afl.data.Team;
import gaj.afl.data.DataFactory;
import gaj.afl.data.Fixture;
import gaj.afl.data.Location;
import gaj.afl.data.Match;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class HomeTeamAdvantage {

   public static final File DATA_FILE = new File("data/derived/home-teams.csv");

   public static void main(String[] args) throws IOException {
      int numData = 0;
      double sumHomeWins = 0, sumSqHomeWins = 0;
      int numCrowsPowerMatches = 0;
      double sumCrowsWins = 0, sumSqCrowsWins = 0;
      Map<Team, Map<Location,Integer>> map = new HashMap<Team, Map<Location,Integer>>();
      Collection<Match> records = DataFactory.newManager().getAllMatches();
      for (Match match : records) {
    	  Fixture fixture = match.getFixture();
         if (!fixture.getRound().toString().startsWith("R")) continue;
         numData++;
         double homeWin = getHomeWin(match);
         sumHomeWins += homeWin;
         sumSqHomeWins += homeWin * homeWin;
         Team homeTeam = fixture.getHomeTeam();
         if (homeTeam == Team.Adelaide && fixture.getAwayTeam() == Team.Port_Adelaide) {
            numCrowsPowerMatches++;
            sumCrowsWins += homeWin;
            sumSqCrowsWins += homeWin * homeWin;
         } else if (homeTeam == Team.Port_Adelaide && fixture.getAwayTeam() == Team.Adelaide) {
            numCrowsPowerMatches++;
            sumCrowsWins += 1 - homeWin;
            sumSqCrowsWins += (1 - homeWin) * (1 - homeWin);
         }
         Map<Location, Integer> locs = map.get(homeTeam);
         if (locs == null)
            map.put(homeTeam, locs = new HashMap<Location,Integer>());
         Location location = fixture.getLocation();
         Integer count = locs.get(location);
         locs.put(location, (count == null) ? 1 : (count+1));
      }
      double meanHomeWins = sumHomeWins / numData;
      double varHomeWins = sumSqHomeWins / numData - meanHomeWins * meanHomeWins;
      double devMeanHomeWins = Math.sqrt(varHomeWins / numData);
      System.out.printf("Proportion of home-wins: mean=%4.2f, interval=(%4.2f,%4.2f)\n",
                        meanHomeWins, meanHomeWins-2*devMeanHomeWins, meanHomeWins+2*devMeanHomeWins);
      double meanCrowsWins = sumCrowsWins / numCrowsPowerMatches;
      double varCrowsWins = sumSqCrowsWins / numCrowsPowerMatches - meanCrowsWins * meanCrowsWins;
      double devMeanCrowsWins = Math.sqrt(varCrowsWins / numCrowsPowerMatches);
      System.out.printf("Proportion of Crows-wins over Power: mean=%4.2f, interval=(%4.2f,%4.2f)\n",
                        meanCrowsWins, meanCrowsWins-2*devMeanCrowsWins, meanCrowsWins+2*devMeanCrowsWins);
      File path = DATA_FILE.getParentFile();
      if (!path.exists()) path.mkdirs();
      PrintStream out = new PrintStream(DATA_FILE);
      List<Team> teams = new ArrayList<Team>(map.keySet());
      Collections.sort(teams);
      System.out.printf("Proportion of home games played at each location, per team:\n");
      for (Team team : teams) {
         Map<Location, Integer> locs = map.get(team);
         Location home = null;
         int maxCount = 0;
         int sumCount = 0;
         for (Entry<Location, Integer> entry : locs.entrySet()) {
            int count = entry.getValue();
            sumCount += count;
            if (count > maxCount) {
               maxCount = count;
               home = entry.getKey();
            }
         }
         //System.out.printf("%s -> %s [%4.2f]\n", team, home, 1.0*maxCount/sumCount);
         System.out.printf("%s -> [ ", team);
         out.printf("%s", team.name());
         for (Entry<Location, Integer> entry : locs.entrySet()) {
            int count = entry.getValue();
            Location loc = entry.getKey();
            if (loc == home) System.out.printf("*");
            double prob = (double)count/sumCount;
            System.out.printf("%s=%4.2f ", loc.name(), prob);
            out.printf(",%s=%f", loc.name(), prob);
         }
         System.out.printf("]\n");
         out.printf("\n");
      }
      out.close();
   }

   private static double getHomeWin(Match record) {
      switch (record.getOutcome()) {
      case Draw:
         return 0.5;
      case Loss:
         return 0.0;
      case Win:
         return 1.0;
      default:
         throw new IllegalArgumentException("Unknown result: " + record.getOutcome());
      }
   }

}
