package afl.analyser;

import gaj.afl.data.finalsiren.OldFinalSirenScraper;
import gaj.afl.data.match.MatchRecord;
import gaj.afl.data.match.MatchResult;
import gaj.afl.data.match.Team;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AwayStats {

	public WinLossStats goals, behinds, points;

	//========================================================================================
	public static void main(String[] args) throws IOException {
		WinLossStats overall = new WinLossStats();
		Map<Team,WinLossStats> stats = new HashMap<Team,WinLossStats>();
		File dir = new File("data/training/finalsiren/match");
		for (File file : dir.listFiles()) {
			List<MatchRecord> records = OldFinalSirenScraper.scrapeFolder(file.getAbsoluteFile());
			update(stats, records);
			update(overall, records);
		}
		List<Team> teams = new ArrayList<Team>(stats.keySet());
		Collections.sort(teams);
		for (Team team : teams) {
			System.out.printf("%s: %s\n", team, stats.get(team));
		}
		System.out.printf("Overall: %s\n", overall);
	}

	public static void update(Map<Team, WinLossStats> records, List<MatchRecord> matches) {
		for (MatchRecord rec : matches) {
			WinLossStats homeStats = records.get(rec.homeTeam.team);
			if (homeStats == null)
				records.put(rec.homeTeam.team, homeStats = new WinLossStats());
			WinLossStats awayStats = records.get(rec.awayTeam.team);
			if (awayStats == null)
				records.put(rec.awayTeam.team, awayStats = new WinLossStats());
			homeStats.add(rec.homeTeamResult, true);
			awayStats.add(rec.homeTeamResult.reverse(), true);
		}
	}

	/**
	 * Updates (accumulates) the statistics for all matches.
	 * Note: The results will have been double-counted!
	 * 
	 * @param stats - The statistics to be updated.
	 * @param matches - The list of match records.
	 */
	public static void update(WinLossStats stats, List<MatchRecord> matches) {
		for (MatchRecord rec : matches) {
			stats.add(rec.homeTeamResult, true);
			stats.add(rec.homeTeamResult.reverse(), false);
		}
	}

}
