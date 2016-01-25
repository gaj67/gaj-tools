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
public class TrainPrevBye {

    private static final MatchFetcher fetcher = MatchDataFactory.getMatchFetcher();

    public static void main(String[] args) {
        analyse();
    }

    private static void analyse() {
        System.out.println("---------------------------------------");
        GoldData trainingData = getMatchData(fetcher.getMatches(2000, 2004, 2008, 2009, 2010, 2011));
        GoldData testingData1 = getMatchData(fetcher.getMatches(2001, 2012, 2013));
        GoldData testingData2 = getMatchData(fetcher.getMatches(2012));
        GoldData testingData3 = getMatchData(fetcher.getMatches(2013));
        LoggedClassifierTrainer.getTrainer(trainingData, testingData1, testingData2, testingData3).train(20);
    }

    private static GoldData getMatchData(final Collection<Match> matches) {
        return new GoldMatchDataNoDraws(matches) {
            @Override
            public int numFeatures() {
                return 3;
            }

            @Override
            protected DataVector getFeatures(Match match) {
                double[] features = new double[3];
                features[0] = 1;
                Fixture fixture = match.getFixture();
                int year = getYear(fixture);
                Round round = fixture.getRound();
                features[1] = getPlayedLastRoundFeature(getPrevNOutcomes(fixture.getHomeTeam(), year, round, 1));
                features[2] = getPlayedLastRoundFeature(getPrevNOutcomes(fixture.getAwayTeam(), year, round, 1));
                return VectorFactory.newVector(features);
            }

        };
    }

    // 1 = played last round, 0 = didn't play.
    protected static double getPlayedLastRoundFeature(List<Outcome> prevNOutcomes) {
        return prevNOutcomes.isEmpty() ? 0 : (prevNOutcomes.get(0) == Outcome.Bye) ? 0 : 1;
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
