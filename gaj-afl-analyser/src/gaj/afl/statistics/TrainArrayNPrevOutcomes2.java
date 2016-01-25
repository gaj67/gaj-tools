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
import gaj.data.classifier.GoldData;
import gaj.data.vector.DataVector;
import gaj.impl.vector.VectorFactory;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * Tests the effect for each team of an array of outcomes of their previous N matches.
 */
public class TrainArrayNPrevOutcomes2 {

    private static final int NUM_OUTCOMES = Outcome.values().length;

    private static final MatchFetcher fetcher = MatchDataFactory.getMatchFetcher();
    private static final int MAX_NUM_OUTCOMES = 10;

    public static void main(String[] args) {
        for (int numOutcomes = 0; numOutcomes <= MAX_NUM_OUTCOMES; numOutcomes++) {
            analyse(numOutcomes);
        }
    }

    private static void analyse(int numOutcomes) {
        System.out.println("---------------------------------------");
        System.out.printf("Analysing over the history of the previous %d matches...%n", numOutcomes);
        GoldData trainingData = getMatchData(fetcher.getMatches(2000, 2004, 2008, 2009, 2010, 2011), numOutcomes);
        GoldData testingData1 = getMatchData(fetcher.getMatches(2001, 2012, 2013), numOutcomes);
        GoldData testingData2 = getMatchData(fetcher.getMatches(2012), numOutcomes);
        GoldData testingData3 = getMatchData(fetcher.getMatches(2013), numOutcomes);
        LoggedClassifierTrainer.getTrainer(trainingData, testingData1, testingData2, testingData3).train(20);
    }

    private static final DataVector ONE_VECTOR = VectorFactory.newFixedVector(1, 1);

    private static GoldData getMatchData(final Collection<Match> matches, final int numOutcomes) {
        return new GoldMatchDataNoDraws(matches) {
            @Override
            public int numFeatures() {
                return 1 + NUM_OUTCOMES;
            }

            @Override
            protected DataVector getFeatures(Match match) {
                Fixture fixture = match.getFixture();
                return VectorFactory.concatenate(
                        ONE_VECTOR,
                        getDifferenceFeatures(fixture, numOutcomes));
            }

        };
    }

    private static DataVector getDifferenceFeatures(Fixture fixture, int numOutcomes) {
        DataVector homeFeatures = getPrevNOutcomesFeatures(fixture.getHomeTeam(), fixture, numOutcomes);
        DataVector awayFeatures = getPrevNOutcomesFeatures(fixture.getAwayTeam(), fixture, numOutcomes);
        return VectorFactory.add(homeFeatures, VectorFactory.scale(awayFeatures, -1));
    }

    private static DataVector getPrevNOutcomesFeatures(Team team, Fixture fixture, int maxNumOutcomes) {
        int[] counts = new int[NUM_OUTCOMES];
        List<Outcome> outcomes = getPrevNOutcomes(team, getYear(fixture), fixture.getRound(), maxNumOutcomes);
        for (Outcome outcome : outcomes) {
            counts[outcome.ordinal()]++;
        }
        double[] features = new double[NUM_OUTCOMES];
        final int N = outcomes.size();
        if (N > 0) {
            for (int i = 0; i < NUM_OUTCOMES; i++) {
                features[i] = 1.0 * counts[i] / N;
            }
        }
        return VectorFactory.newVector(features);
    }

    private static List<Outcome> getPrevNOutcomes(Team team, int year, Round round, final int maxNumOutcomes) {
        List<Outcome> outcomes = new ArrayList<>(maxNumOutcomes);
        int numOutcomes = 0;
        Round prevRound = round.prevRound();
        while (prevRound != null && numOutcomes < maxNumOutcomes) {
            Match prevMatch = fetcher.getMatch(team, year, prevRound);
            if (prevMatch != null) {
                Outcome outcome = prevMatch.getOutcome();
                outcomes.add((team == prevMatch.getFixture().getHomeTeam()) ? outcome : outcome.reverse());
            } else {
                outcomes.add(Outcome.Bye);
            }
            numOutcomes++;
            prevRound = prevRound.prevRound();
        }
        return outcomes;
    }

    private static int getYear(Fixture fixture) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fixture.getDateTime());
        int year = calendar.get(Calendar.YEAR);
        return year;
    }
}
