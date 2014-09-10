package gaj.afl.statistics;
import gaj.afl.data.MatchDataFactory;
import gaj.afl.data.core.Fixture;
import gaj.afl.data.core.Match;
import gaj.afl.data.core.Outcome;
import gaj.afl.data.core.Round;
import gaj.afl.data.core.Team;
import gaj.afl.data.store.MatchFetcher;
import gaj.data.matrix.WritableMatrix;
import gaj.data.vector.DataVector;
import gaj.data.vector.WritableVector;
import gaj.impl.matrix.MatrixFactory;
import gaj.impl.vector.VectorFactory;
import java.util.Calendar;
import java.util.Collection;


/**
 * Tests for home-ground advantage by training a classifier with a constant feature value of 1.
 */
public class SummariseData {

    private static final MatchFetcher fetcher = MatchDataFactory.getMatchFetcher();

    public static void main(String[] args) {
	// Collect all match statistics...
	Collection<Match> matches = fetcher.getMatches(2010, 2011, 2012);
	summariseHomeWinsLosses(matches);
	summarisePrevWinLoss(matches);
	summariseHomeOutcomeVersusPrevOutcome(matches);
	summariseHomeOutcomeVersusPrevAwayOutcome(matches);
	summarisePrevHomeOutcomeVersusPrevAwayOutcome(matches);
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
	displayOutcomes();
	for (Match match : matches) {
	    Fixture fixture = match.getFixture();
	    int prevHomeOutcomeIndex = getPrevOutcomeIndex(fixture.getHomeTeam(), fixture);
	    int prevAwayOutcomeIndex = getPrevOutcomeIndex(fixture.getAwayTeam(), fixture);
	    if (prevHomeOutcomeIndex < 0 || prevAwayOutcomeIndex < 0) {
		continue;
	    }
	    switch (match.getOutcome()) {
		case Draw:
		    winCounts.add(prevHomeOutcomeIndex, prevAwayOutcomeIndex, 0.5);
		    winCounts.add(prevAwayOutcomeIndex, prevHomeOutcomeIndex, 0.5);
		    break;
		case Loss:
		    winCounts.add(prevAwayOutcomeIndex, prevHomeOutcomeIndex, 1);
		    break;
		case Win:
		    winCounts.add(prevHomeOutcomeIndex, prevAwayOutcomeIndex, 1);
		    break;
		default:
		    break;
	    }
	}
	MatrixFactory.display("Prev. outcome counts=", winCounts, "\n");
    }

    private static void displayOutcomes() {
	System.out.printf("Element order: [ ");
	for (Outcome outcome : Outcome.values()) {
	    System.out.printf("%s ", outcome);
	}
	System.out.println("]");
    }

    private static int getPrevOutcomeIndex(Team homeTeam, Fixture fixture) {
	Outcome outcome = getPrevOutcome(homeTeam, fixture);
	return (outcome == null) ? -1 : outcome.ordinal();
    }

    // Obtains the match outcome for the team in the previous round (allowing for possible byes).
    private static /*@Nullable*/ Outcome getPrevOutcome(Team team, Fixture fixture) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(fixture.getDateTime());
	int year = calendar.get(Calendar.YEAR);
	Round prevRound = fixture.getRound().prevRound();
	while (prevRound != null) {
	    Match prevMatch = fetcher.getMatch(team, year, prevRound);
	    if (prevMatch == null) {
		prevRound = prevRound.prevRound();
	    } else {
		return prevMatch.getOutcome();
	    }
	}
	return null;
    }

    /*
     * Summarises the conditional effect on the
     * chances of the home team winning given the outcome of their previous game.
     */
    private static void summariseHomeOutcomeVersusPrevOutcome(Collection<Match> matches) {
	final int N = Outcome.values().length;
	// Current outcome (rows) versus previous outcome (columns).
	WritableMatrix counts = MatrixFactory.newMatrix(N, N);
	displayOutcomes();
	for (Match match : matches) {
	    Outcome prevOutcome = getPrevOutcome(match.getFixture().getHomeTeam(), match.getFixture());
	    if (prevOutcome != null) {
		counts.add(match.getOutcome().ordinal(), prevOutcome.ordinal(), 1);
	    }
	}
	MatrixFactory.display("Home-team counts of current vs previous outcome: ", counts, "\n");
	for (int i = 0; i < Outcome.values().length; i++) {
	    WritableVector column = counts.getColumn(i);
	    DataVector normed = VectorFactory.scale(column, 1./column.sum());
	    VectorFactory.display("P(outcome|prev. " + Outcome.values()[i] + ") = ", normed, "\n");
	}
    }

    /*
     * Summarises the conditional effect on the
     * chances of the home team winning given the outcome of their opponent's previous game.
     */
    private static void summariseHomeOutcomeVersusPrevAwayOutcome(Collection<Match> matches) {
	final int N = Outcome.values().length;
	// Current home outcome (rows) versus previous away outcome (columns).
	WritableMatrix counts = MatrixFactory.newMatrix(N, N);
	displayOutcomes();
	for (Match match : matches) {
	    Fixture fixture = match.getFixture();
	    int curHomeOutcomeIndex = match.getOutcome().ordinal();
	    int prevAwayOutcomeIndex = getPrevOutcomeIndex(fixture.getAwayTeam(), fixture);
	    if (prevAwayOutcomeIndex >= 0) {
		counts.add(curHomeOutcomeIndex, prevAwayOutcomeIndex, 1);
	    }
	}
	MatrixFactory.display("Counts of current home-team outcome vs previous away-team outcome: ", counts, "\n");
	for (int i = 0; i < Outcome.values().length; i++) {
	    WritableVector column = counts.getColumn(i);
	    DataVector normed = VectorFactory.scale(column, 1./column.sum());
	    VectorFactory.display("P(home outcome|prev. away " + Outcome.values()[i] + ") = ", normed, "\n");
	}
    }

    private static void summarisePrevHomeOutcomeVersusPrevAwayOutcome(Collection<Match> matches) {
	WritableMatrix[] counts = new WritableMatrix[] {
		MatrixFactory.newMatrix(3, 3),
		MatrixFactory.newMatrix(3, 3),
		MatrixFactory.newMatrix(3, 3),
	};
	displayOutcomes();
	for (Match match : matches) {
	    Fixture fixture = match.getFixture();
	    int prevHomeOutcomeIndex = getPrevOutcomeIndex(fixture.getHomeTeam(), fixture);
	    int prevAwayOutcomeIndex = getPrevOutcomeIndex(fixture.getAwayTeam(), fixture);
	    if (prevHomeOutcomeIndex >= 0 && prevAwayOutcomeIndex >= 0) {
		counts[match.getOutcome().ordinal()].add(prevHomeOutcomeIndex, prevAwayOutcomeIndex, 1);
	    }
	}
	WritableMatrix totals = MatrixFactory.newMatrix(3, 3);
	for (int i = 0; i < 3; i++) {
	    MatrixFactory.display(
		    "C(home-team " + Outcome.values()[i] + " | home-team prev. outcome vs away-team prev. outcome):\n",
		    counts[i], "\n");
	    totals.add(counts[i]);
	}
	WritableMatrix homeWins = counts[Outcome.Win.ordinal()];
	for (Outcome homeOutcome : Outcome.values()) {
	    if (homeOutcome == Outcome.Draw) {
		continue;
	    }
	    for (Outcome awayOutcome : Outcome.values()) {
		if (awayOutcome == Outcome.Draw) {
		    continue;
		}
		System.out.printf("P(home-team Win|prev. home-team %s, prev. away-team %s)=%f%n",
			homeOutcome, awayOutcome,
			homeWins.get(homeOutcome.ordinal(), awayOutcome.ordinal())
			/ totals.get(homeOutcome.ordinal(), awayOutcome.ordinal())
			);
	    }
	}
    }

}
