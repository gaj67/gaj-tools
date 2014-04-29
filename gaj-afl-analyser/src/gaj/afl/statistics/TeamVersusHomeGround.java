package gaj.afl.statistics;

import gaj.afl.data.MatchDataFactory;
import gaj.afl.data.match.Location;
import gaj.afl.data.match.Match;
import gaj.afl.data.match.Round;
import gaj.afl.data.match.Team;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/*package-private*/ abstract class TeamVersusHomeGround {

	private TeamVersusHomeGround() {}
	
	public static void main(String[] args) {
		Collection<Match> matches = getData();
		Map<Team, Map<Location, Integer>> countsOfTL = getTeamLocationCounts(matches);
		Map<Location, Integer> locationDates = getLocationDates(matches);
		summariseCounts(locationDates, countsOfTL);
		int[][] confusion = computeConfusion(countsOfTL);
		summariseGroups(analyseGroups(confusion, locationDates), locationDates);
	}

	// Try to find groups of potential location aliases.
	private static Map<Location, Set<Location>> analyseGroups(int[][] confusion, Map<Location, Integer> locationDates) {
		Map<Location, Set<Location>> groups = new HashMap<>();
		createGroups(confusion, locationDates, groups);
		filterGroups(groups);
		return groups;
	}

	private static void filterGroups(Map<Location, Set<Location>> groups) {
		for (Entry<Location, Set<Location>> entry : groups.entrySet()) {
			Location loc1 = entry.getKey();
			Iterator<Location> iter = entry.getValue().iterator();
			while (iter.hasNext()) {
				Location loc2 = iter.next();
				Set<Location> group2 = groups.get(loc2);
				if (group2 == null || !group2.contains(loc1))
					iter.remove();
			}
		}
	}

	private static void createGroups(int[][] confusion,
			Map<Location, Integer> locationDates,
			Map<Location, Set<Location>> groups) {
		for (Location loc1 : Location.values()) {
			final int idx1 = loc1.ordinal();
			int count1 = confusion[idx1][idx1];
			int year1 = locationDates.get(loc1);
			for (Location loc2 : Location.values()) {
				if (loc2 == loc1) continue;
				final int idx2 = loc2.ordinal();
				int year2 = locationDates.get(loc2);
				int count2 = confusion[idx1][idx2];
				if (count2 == count1 && year2 != year1) {
					Set<Location> group = groups.get(loc1);
					if (group == null) {
						group = new HashSet<>();
						groups.put(loc1, group);
					}
					group.add(loc2);
				}
			}
		}
	}

	private static void summariseGroups(Map<Location, Set<Location>> groups, Map<Location, Integer> locationDates) {
		System.out.println("***********************");
		System.out.println("Potential alias groups:");
		for (Entry<Location, Set<Location>> entry : groups.entrySet()) {
			if (entry.getValue().size() == 0) continue;
			Location loc1 = entry.getKey();
			System.out.printf("* %s [%d]%n", loc1, locationDates.get(loc1));
			for (Location loc2 : entry.getValue()) {
				System.out.printf("  -> %s [%d]%n", loc2, locationDates.get(loc2));
			}
		}		
	}

	private static int[][] computeConfusion(
			Map<Team, Map<Location, Integer>> countsOfTL) {
		final int L = Location.values().length;
		int[][] confusion = new int[L][L];
		for (Location loc1 : Location.values()) {
			final int idx1 = loc1.ordinal();
			for (Map<Location, Integer> teamLocs : countsOfTL.values()) {
				if (!teamLocs.containsKey(loc1)) continue;
				for (Location loc2 : Location.values()) {
					if (teamLocs.containsKey(loc2)) {
						final int idx2 = loc2.ordinal();
						confusion[idx1][idx2]++;
					}
				}
			}
		}
		return confusion;
	}

	private static Map<Location, Integer> getLocationDates(Collection<Match> matches) {
		Calendar cal = Calendar.getInstance();
		Map<Location, Integer> dates = new HashMap<>();
		for (Match record : matches) {
			Location location = record.getFixture().getLocation();
		    cal.setTime(record.getFixture().getDateTime());
		    int year = cal.get(Calendar.YEAR);
			Integer latestYear = dates.get(location);
			latestYear = (latestYear == null) 
					? year 
					: Math.max(latestYear, year);
			dates.put(location, latestYear);
		}
		return dates;
	}

	private static void summariseCounts(
			Map<Location, Integer> datesOfL, 
			Map<Team, Map<Location, Integer>> countsOfTL) 
	{
		for (Entry<Team, Map<Location, Integer>> oentry : countsOfTL.entrySet()) {
			System.out.printf("Team: %s%n", oentry.getKey());
			for (Entry<Location, Integer> ientry : oentry.getValue().entrySet()) {
				System.out.printf("   * %s -> %d [%d]%n", 
						ientry.getKey(), ientry.getValue(),
						datesOfL.get(ientry.getKey()));
			}
		}
	}

	private static Collection<Match> getData() {
		return MatchDataFactory.newManager().getMatches();
	}
	
	private static Map<Team, Map<Location, Integer>> getTeamLocationCounts(Collection<Match> matches) {
		Map<Team, Map<Location, Integer>> countsOfTL = new HashMap<>();
		for (Match record : matches) {
			Round round = record.getFixture().getRound();
			if (!round.toString().startsWith("R")) continue;
			Team team = record.getFixture().getHomeTeam();
			Map<Location, Integer> countsOfL = countsOfTL.get(team);
			if (countsOfL == null) {
				countsOfL = new HashMap<Location, Integer>();
				countsOfTL.put(team, countsOfL);
			}
			Location location = record.getFixture().getLocation();
			Integer count = countsOfL.get(location);
			countsOfL.put(location, (count == null) ? 1 : (count + 1));
		}
		return countsOfTL;
	}
}
