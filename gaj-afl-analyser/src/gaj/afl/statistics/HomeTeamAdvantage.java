package gaj.afl.statistics;
import gaj.afl.data.MatchDataFactory;
import gaj.afl.data.core.Match;
import gaj.afl.data.core.Team;
import java.util.Collection;



public class HomeTeamAdvantage {

    public static void main(String[] args) {
	final int NUM_TEAMS = Team.values().length;
	// Summary statistics about home team:
	int[] homeWins = new int[NUM_TEAMS];
	int[] homeDraws = new int[NUM_TEAMS];
	int[] homeLosses = new int[NUM_TEAMS];
	// Summary statistics about away team:
	int[] awayWins = new int[NUM_TEAMS];
	int[] awayDraws = new int[NUM_TEAMS];
	int[] awayLosses = new int[NUM_TEAMS];
	// Collect statistics...
	Collection<Match> records = MatchDataFactory.getMatchFetcher().getMatches();
	for (Match match : records) {
	    int homeIdx = match.getFixture().getHomeTeam().ordinal();
	    int awayIdx = match.getFixture().getAwayTeam().ordinal();
	    switch (match.getOutcome()) {
		case Draw:
		    homeDraws[homeIdx]++;
		    awayDraws[awayIdx]++;
		    break;
		case Loss:
		    homeLosses[homeIdx]++;
		    awayWins[awayIdx]++;
		    break;
		case Win:
		    homeWins[homeIdx]++;
		    awayLosses[awayIdx]++;
		    break;
		default:
		    break;
	    }
	}
	// Summarise statistics...
	displayHeader();
	int totWins = 0, totDraws = 0, totLosses = 0;
	for (Team team : Team.values()) {
	    int teamIdx = team.ordinal();
	    displayStats(team.toExternal(),
		    homeWins[teamIdx], homeDraws[teamIdx], homeLosses[teamIdx],
		    awayWins[teamIdx], awayDraws[teamIdx], awayLosses[teamIdx]);
	    totWins += homeWins[teamIdx];
	    totDraws += homeDraws[teamIdx];
	    totLosses += homeLosses[teamIdx];
	}
	displayStats("Total",  totWins, totDraws, totLosses,
		totLosses, totDraws, totWins);
    }

    private static void displayHeader() {
	System.out.println("Team: home-wins, draws, losses, games, advantage, away-wins, draws, losses, games, advantage");
    }

    private static void displayStats(
	    String label, int homeWins, int homeDraws, int homeLosses,
	    int awayWins, int awayDraws, int awayLosses)
    {
	int homeGames = homeWins + homeDraws + homeLosses;
	double homeAdv = (homeWins + 0.5 * homeDraws) / homeGames - 0.5;
	int awayGames = awayWins + awayDraws + awayLosses;
	double awayAdv = (awayWins + 0.5 * awayDraws) / awayGames - 0.5;
	System.out.printf("* %s: %d, %d, %d, %d, %4.2f; %d, %d, %d, %d, %4.2f%n",
		label, homeWins, homeDraws, homeLosses, homeGames, homeAdv,
		awayWins, awayDraws, awayLosses, awayGames, awayAdv);
    }

}
