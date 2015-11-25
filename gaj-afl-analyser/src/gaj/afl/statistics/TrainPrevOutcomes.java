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
import java.util.Calendar;
import java.util.Collection;

/**
 * Tests the effect for each team of the outcome of their previous match.
 */
public class TrainPrevOutcomes {

    private static final MatchFetcher fetcher = MatchDataFactory.getMatchFetcher();

    public static void main(String[] args) {
        // Collect all match statistics...
        GoldData trainingData = getMatchData(fetcher.getMatches(2000, 2004, 2008, 2009, 2010, 2011));
        int n = 0, w = 0;
        for (GoldDatum datum : trainingData) {
            n++;
            if (datum.getClassIndex() == 1) {
                w++;
            }
        }
        double p = 1.0 * w / n;
        System.out.printf("#games=%d, home-losses=%d, home-wins=%d, P(home-win)=%5.3f, P(home-loss)=%5.3f%n", n, n - w, w, p, 1 - p);
        System.out.printf("Expected parameter=%f%n", Math.log((1 - p) / p));
        GoldData testingData = getMatchData(fetcher.getMatches(2001, 2012, 2013));
        LoggedClassifierTrainer.getTrainer(trainingData, testingData, AccelerationType.Quadratic).train(20);
    }

    private static GoldData getMatchData(final Collection<Match> matches) {
        return new GoldMatchDataNoDraws(matches) {
            @Override
            public int numFeatures() {
                return 3;
            }

            @Override
            protected DataVector getFeatures(Match match) {
                Fixture fixture = match.getFixture();
                return VectorFactory.newVector(
                        1.,
                        getPrevOutcomeFeature(fixture.getHomeTeam(), fixture),
                        getPrevOutcomeFeature(fixture.getAwayTeam(), fixture));
            }
        };
    }

    // Obtains the match outcome for the team in the previous round (allowing for possible byes).
    private static/* @Nullable */Outcome getPrevOutcome(Team team, Fixture fixture) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fixture.getDateTime());
        int year = calendar.get(Calendar.YEAR);
        Round prevRound = fixture.getRound().prevRound();
        while (prevRound != null) {
            Match prevMatch = fetcher.getMatch(team, year, prevRound);
            if (prevMatch == null) {
                prevRound = prevRound.prevRound();
            } else {
                Outcome outcome = prevMatch.getOutcome();
                return (team == prevMatch.getFixture().getHomeTeam()) ? outcome : outcome.reverse();
            }
        }
        return null;
    }

    private static double getPrevOutcomeFeature(Team team, Fixture fixture) {
        Outcome outcome = getPrevOutcome(team, fixture);
        return (outcome == null) ? 0 : (Outcome.Win == outcome) ? 1 : (Outcome.Loss == outcome) ? -1 : 0;
    }
}
