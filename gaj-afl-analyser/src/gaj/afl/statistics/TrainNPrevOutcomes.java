package gaj.afl.statistics;
import gaj.afl.classifier.GoldMatchDataNoDraws;
import gaj.afl.classifier.LoggedClassifierTrainer;
import gaj.afl.data.MatchDataFactory;
import gaj.afl.data.core.Fixture;
import gaj.afl.data.core.Match;
import gaj.afl.data.core.Outcome;
import gaj.afl.data.core.Round;
import gaj.afl.data.core.Team;
import gaj.afl.data.store.MatchFetcher;
import gaj.analysis.classifier.AccelerationType;
import gaj.data.classifier.GoldData;
import gaj.data.classifier.GoldDatum;
import gaj.data.vector.DataVector;
import gaj.impl.vector.VectorFactory;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;


/**
 * Tests the effect for each team of the outcomes of their previous N matches.
 */
public class TrainNPrevOutcomes {

    private static final MatchFetcher fetcher = MatchDataFactory.getMatchFetcher();
    private static final int MAX_NUM_OUTCOMES = 5;

    public static void main(String[] args) {
	for (int numOutcomes = 1; numOutcomes < MAX_NUM_OUTCOMES; numOutcomes++) {
	    analyse(numOutcomes);
	}
    }

    private static void analyse(int numOutcomes) {
	System.out.println("---------------------------------------");
	System.out.printf("Analysing over the history of the previous %d matches...%n", numOutcomes);
	GoldData trainingData = getMatchData(fetcher.getMatches(2008, 2009, 2010, 2011), numOutcomes);
	int n = 0, w = 0;
	for (GoldDatum datum : trainingData) {
	    n++;
	    if (datum.getClassIndex() == 1) {
		w++;
	    }
	}
	double p = 1.0 * w / n;
	System.out.printf("#games=%d, home-losses=%d, home-wins=%d, P(home-win)=%5.3f, P(home-loss)=%5.3f%n", n, n-w, w, p, 1-p);
	GoldData testingData = getMatchData(fetcher.getMatches(2012, 2013), numOutcomes);
	LoggedClassifierTrainer.getTrainer(trainingData, testingData, AccelerationType.Quadratic).train(20);
    }

    private static GoldData getMatchData(final Collection<Match> matches, final int numOutcomes) {
	return new GoldMatchDataNoDraws(matches) {
	    @Override
	    public int numFeatures() {
		return 2;
	    }

	    @Override
	    protected DataVector getFeatures(Match match) {
		Fixture fixture = match.getFixture();
		return VectorFactory.newVector(
			1.,
			getPrevNOutcomesFeature(fixture.getHomeTeam(), fixture, numOutcomes) -
			getPrevNOutcomesFeature(fixture.getAwayTeam(), fixture, numOutcomes));
	    }
	};
    }

    private static double getPrevNOutcomesFeature(Team team, Fixture fixture, int numOutcomes) {
	return calcWinsProportion(getPrevNOutcomes(team, fixture, numOutcomes));
    }

    private static List<Outcome> getPrevNOutcomes(Team team, Fixture fixture, int numOutcomes) {
	List<Outcome> outcomes = new ArrayList<>(numOutcomes);
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(fixture.getDateTime());
	int year = calendar.get(Calendar.YEAR);
	int numMatches = 0;
	Round prevRound = fixture.getRound().prevRound();
	while (prevRound != null && numMatches < numOutcomes) {
	    Match prevMatch = fetcher.getMatch(team, year, prevRound);
	    if (prevMatch != null) {
		Outcome outcome = prevMatch.getOutcome();
		outcomes.add((team == prevMatch.getFixture().getHomeTeam()) ? outcome : outcome.reverse());
		numMatches++;
	    }
	    prevRound = prevRound.prevRound();
	}
	return outcomes;
    }

    private static double calcWinsProportion(List<Outcome> outcomes) {
	if (outcomes.isEmpty()) {
	    return 0;
	}
	double wins = 0;
	for (Outcome outcome : outcomes) {
	    wins += (Outcome.Win == outcome) ? 1 : (Outcome.Loss == outcome) ? 0 : 0.5;
	}
	return wins / outcomes.size();
    }
}
