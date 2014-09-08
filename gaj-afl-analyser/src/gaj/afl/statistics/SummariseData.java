package gaj.afl.statistics;
import gaj.afl.data.MatchDataFactory;
import gaj.afl.data.match.Match;
import gaj.afl.data.match.MatchFetcher;
import gaj.afl.data.match.Outcome;
import gaj.data.matrix.WritableMatrix;
import gaj.impl.matrix.MatrixFactory;
import java.util.Collection;


/**
 * Tests for home-ground advantage by training a classifier with a constant feature value of 1.
 */
public class SummariseData {

    public static void main(String[] args) {
	// Collect all match statistics...
	MatchFetcher fetcher = MatchDataFactory.getMatchFetcher();
	Collection<Match> trainingData = fetcher.getMatches(2010, 2011, 2012);
	summariseHomeWinsLosses(trainingData);
	summarisePrevWinLoss(trainingData);
    }

    /*
     *  Summarises the effect of any home-ground advantage.
     */
    private static void summariseHomeWinsLosses(Collection<Match> matches) {
	int n = 0, w = 0;
	for (Match match : matches) {
	    n++;
	    if (Outcome.Win == match.getOutcome()) {
		w++;
	    }
	    /*System.out.printf("Match[%d]: home=%s, away=%s, outcome=%s%n",
		    n, match.getHomeTeamScores(), match.getAwayTeamScores(), match.getOutcome());*/
	}
	double p = 1.0 * w / n;
	System.out.printf("#games=%d, home-losses=%d, home-wins=%d, P(home-win)=%5.3f, P(home-loss)=%5.3f%n", n, n-w, w, p, 1-p);
    }

    /*
     * Summarises the effect of having won or lost the previous game.
     * For simplicity, it is assumed there are no previous games at the start of each year.
     * The counts are accumulated from the perspective of the winning team, with
     * the row corresponding to the previous win/loss/draw of the winning team, and the
     * column corresponding to the losing team. Drawn matches count for half each.
     */
    private static void summarisePrevWinLoss(Collection<Match> matches) {
	WritableMatrix winCounts = MatrixFactory.newMatrix(3, 3);
	System.out.printf("Element order: [ ");
	for (Outcome outcome : Outcome.values()) {
	    System.out.printf("%s ", outcome);
	}
	System.out.println("]");
	for (Match match : matches) {
	    Outcome outcome = match.getOutcome();
	}
    }

}
