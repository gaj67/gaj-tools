package gaj.afl.statistics;

import gaj.afl.classifier.GoldMatchDataNoDraws;
import gaj.afl.classifier.LoggedClassifierTrainer;
import gaj.afl.data.MatchDataFactory;
import gaj.afl.data.core.Location;
import gaj.afl.data.core.Match;
import gaj.afl.data.core.Team;
import gaj.afl.data.store.MatchFetcher;
import gaj.analysis.classifier.AccelerationType;
import gaj.analysis.classifier.ClassifierFactory;
import gaj.data.classifier.GoldData;
import gaj.data.classifier.GoldDatum;
import gaj.data.vector.DataVector;
import gaj.impl.vector.VectorFactory;
import java.util.Collection;

/**
 * Tests for home-ground advantage by training a classifier with a distance-travelled measure for each team.
 */
public class TrainHomeTeamAdvantage3 {

    public static void main(String[] args) {
        // Collect all match statistics...
        MatchFetcher manager = MatchDataFactory.getMatchFetcher();
        GoldData trainingData = getMatchData(manager.getMatches(2008, 2009, 2010, 2011));
        int n = 0, w = 0;
        for (GoldDatum datum : trainingData) {
            n++;
            if (datum.getClassIndex() == 1)
                w++;
        }
        double p = 1.0 * w / n;
        System.out.printf("#games=%d, home-losses=%d, home-wins=%d, P(home-win)=%5.3f, P(home-loss)=%5.3f%n", n, n - w, w, p, 1 - p);
        System.out.printf("Expected parameter=%f%n", Math.log((1 - p) / p));
        GoldData testingData = getMatchData(manager.getMatches(2012, 2013));
        LoggedClassifierTrainer.getTrainer(trainingData, testingData, AccelerationType.Linear).train(500);
        LoggedClassifierTrainer.getTrainer(trainingData, testingData, AccelerationType.Quadratic).train(20);
    }

    private static GoldData getMatchData(final Collection<Match> matches) {
        return new GoldMatchDataNoDraws(matches) {
            @Override
            public int numFeatures() {
                return 2;
            }

            @Override
            protected DataVector getFeatures(Match match) {
                Location ground = match.getFixture().getLocation();
                Team homeTeam = match.getFixture().getHomeTeam();
                Location homeLoc = TeamHomeLocation.getTrainingLocation(homeTeam);
                double homeDist = computeDistance(homeLoc, ground);
                Team awayTeam = match.getFixture().getAwayTeam();
                Location awayLoc = TeamHomeLocation.getTrainingLocation(awayTeam);
                double awayDist = computeDistance(awayLoc, ground);
                return VectorFactory.newVector(homeDist, awayDist);
            }
        };
    }

    private static double computeDistance(Location loc1, Location loc2) {
        double x = (loc1.getLongitude() - loc2.getLongitude());
        double y = (loc1.getLatitude() - loc2.getLatitude());
        return Math.sqrt(x * x + y * y);
    }
}
