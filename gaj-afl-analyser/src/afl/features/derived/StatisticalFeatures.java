package afl.features.derived;

import gaj.afl.data.Team;
import gaj.afl.datatype.MatchLocation;
import gaj.afl.statistics.HomeTeamAdvantage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * The features in this class are based directly upon the statistics derived from the pre-2010 season training data.
 */
public class StatisticalFeatures {

   public static Map<Team,Map<MatchLocation,Double>> HOME_ADVANTAGES = new HashMap<Team,Map<MatchLocation,Double>>();
   static {
      try {
         BufferedReader in = new BufferedReader(new FileReader(HomeTeamAdvantage.DATA_FILE));
         String line = null;
         while ((line = in.readLine()) != null) {
            String[] parts = line.split(",");
            Team team = Team.valueOf(parts[0]);
            Map<MatchLocation, Double> map = HOME_ADVANTAGES.get(team);
            if (map == null)
               HOME_ADVANTAGES.put(team, map = new HashMap<MatchLocation,Double>());
            for (int i = 1; i < parts.length; i++) {
               String[] subparts = parts[i].split("=");
               MatchLocation loc = MatchLocation.valueOf(subparts[0]);
               Double prob = Double.valueOf(subparts[1]);
               map.put(loc, prob);
            }
         }
         in.close();
      } catch (IOException e) {
         throw new RuntimeException("Missing or damaged data file! Perhaps run " + HomeTeamAdvantage.class.getSimpleName() + " to (re)create it");
      }
   }

   /**
    * @param team - One of the two teams playing in a match.
    * @param location - The location of the match.
    * @return The strength of any possible home-ground advantage for the given team
    * playing at the given location.
    */
   public static double getHomeAdvantage(Team team, MatchLocation location) {
      Double prob = HOME_ADVANTAGES.get(team).get(location);
      return (prob == null) ? 0 : prob;
   }

   public static void main(String[] args) {
      System.out.printf("Testing home-ground advantage:\n");
      System.out.printf("  - getHomeAdvantage(Adelaide,AAMI_Stadium)=%4.2f\n", getHomeAdvantage(Team.Adelaide, MatchLocation.AAMI_Stadium));
      System.out.printf("  - getHomeAdvantage(Port_Adelaide,AAMI_Stadium)=%4.2f\n", getHomeAdvantage(Team.Port_Adelaide, MatchLocation.AAMI_Stadium));
      System.out.printf("  - getHomeAdvantage(Adelaide,Gabba)=%4.2f\n", getHomeAdvantage(Team.Adelaide, MatchLocation.Gabba));
      System.out.printf("  - getHomeAdvantage(Geelong,Skilled_Stadium)=%4.2f\n", getHomeAdvantage(Team.Geelong, MatchLocation.Skilled_Stadium));
   }

}
