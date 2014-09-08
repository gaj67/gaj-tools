package gaj.afl.statistics;
import gaj.afl.data.MatchDataFactory;
import gaj.afl.data.match.Match;
import gaj.afl.data.match.MatchFetcher;
import gaj.afl.data.match.Outcome;
import java.util.Collection;


/**
 * Tests for home-ground advantage by training a classifier with a constant feature value of 1.
 */
public class SummariseData {

    public static void main(String[] args) {
	// Collect all match statistics...
	MatchFetcher manager = MatchDataFactory.getMatchFetcher();
	Collection<Match> trainingData = manager.getMatchesByYear(2010, 2011, 2012);
	int n = 0, w = 0;
	for (Match match : trainingData) {
	    n++;
	    if (Outcome.Win == match.getOutcome()) {
		w++;
	    }
	    System.out.printf("Match[%d]: home=%s, away=%s, outcome=%s%n",
		    n, match.getHomeTeamScores(), match.getAwayTeamScores(), match.getOutcome());
	}
	double p = 1.0 * w / n;
	System.out.printf("#games=%d, home-losses=%d, home-wins=%d, P(home-win)=%5.3f, P(home-loss)=%5.3f%n", n, n-w, w, p, 1-p);
    }

}
