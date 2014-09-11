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
	Collection<Match> matches = fetcher.getMatches(2005, 2006, 2007, 2008, 2009, 2010, 2011);
	summariseHomeWinsLosses(matches);
	summarisePrevWinLoss(matches);
	//summariseHomeOutcomeVersusPrevOutcome(matches);
	//summariseHomeOutcomeVersusPrevAwayOutcome(matches);
	//summarisePrevHomeOutcomeVersusPrevAwayOutcome(matches);
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
     * Summarises the effect of having won or lost the previous game on the outcome of the current game.
     * For simplicity, it is assumed there are no previous games at the start of each year.
     * Also, a draw is averaged equally across both a win and a loss.
     */
    private static void summarisePrevWinLoss(Collection<Match> matches) {
	// Current vs previous outcomes, home team and away team separately.
	WritableMatrix[] counts = new WritableMatrix[] {
		MatrixFactory.newMatrix(2, 2),
		MatrixFactory.newMatrix(2, 2),
	};
	System.out.printf("Outcomes: %s=%d, %s=%d%n",
		Outcome.Win, Outcome.Win.ordinal(),
		Outcome.Loss, Outcome.Loss.ordinal());
	for (Match match : matches) {
	    Fixture fixture = match.getFixture();
	    Outcome outcome = match.getOutcome();
	    Outcome prevHomeOutcome = getPrevOutcome(fixture.getHomeTeam(), fixture);
	    if (prevHomeOutcome != null) {
		addCounts(counts[0], outcome, prevHomeOutcome);
	    }

	    Outcome curAwayOutcome = toggleOutcome(outcome);
	    Outcome prevAwayOutcome = getPrevOutcome(fixture.getAwayTeam(), fixture);
	    if (prevAwayOutcome != null) {
		addCounts(counts[1], curAwayOutcome, prevAwayOutcome);
	    }
	}
	MatrixFactory.display("Home-team: Cur. outcome vs prev. outcome counts: ", counts[0], "\n");
	MatrixFactory.display("Away-team: Cur. outcome vs prev. outcome counts: ", counts[1], "\n");
	WritableMatrix totals = MatrixFactory.newMatrix(counts[0]);
	totals.add(counts[1]);
	MatrixFactory.display("Overall: Cur. outcome vs prev. outcome counts: ", totals, "\n");
    }

    private static void addCounts(WritableMatrix counts, Outcome curOutcome, Outcome prevOutcome) {
	final boolean isCurDraw = (curOutcome == Outcome.Draw);
	final boolean isPrevDraw = (prevOutcome == Outcome.Draw);
	if (!isCurDraw && !isPrevDraw) {
	    counts.add(curOutcome.ordinal(), prevOutcome.ordinal(), 1);
	} else if (isCurDraw && isPrevDraw) {
	    counts.add(0.25);
	} else if (isCurDraw/*&& !isPrevDraw*/) {
	    counts.getColumn(prevOutcome.ordinal()).add(0.5);
	} else /*isPrevDraw (&& !isCurDraw)*/ {
	    counts.getRow(curOutcome.ordinal()).add(0.5);
	}
    }

    private static Outcome toggleOutcome(Outcome outcome) {
	return (Outcome.Draw == outcome) ? Outcome.Draw : (Outcome.Win == outcome) ? Outcome.Loss : Outcome.Win;
    }

    private static int getOutcomeIndex(/*@Nullable*/ Outcome outcome) {
	return (outcome == null) ? -1 : (Outcome.Loss == outcome) ? 0 : 1;
    }

    private static double getOutcomeWeight(Outcome outcome) {
	return (Outcome.Draw == outcome) ? 0.5 : 1;
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
