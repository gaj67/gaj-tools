package gaj.afl.statistics;
import gaj.afl.classifier.LoggedClassifierTrainer;
import gaj.afl.data.MatchDataFactory;
import gaj.afl.data.classifier.GoldMatchDataNoDraws;
import gaj.afl.data.match.Location;
import gaj.afl.data.match.Match;
import gaj.afl.data.match.MatchFetcher;
import gaj.afl.data.match.Team;
import gaj.analysis.classifier.AccelerationType;
import gaj.data.classifier.GoldData;
import gaj.data.classifier.GoldDatum;
import gaj.data.vector.DataVector;
import gaj.impl.vector.VectorFactory;
import java.util.Collection;


/**
 * Tests for home-ground advantage by training a classifier with a constant feature value of 1 plus
 * an indicator of whether or not the home team actually had to travel to a non-home venue.
 */
public class TrainHomeTeamAdvantage6 {

    public static void main(String[] args) {
	// Collect all match statistics...
	MatchFetcher manager = MatchDataFactory.getMatchFetcher();
	GoldData trainingData = getMatchData(manager.getMatchesByYear(2008, 2009, 2010, 2011));
	int n = 0, w = 0;
	for (GoldDatum datum : trainingData) {
	    n++;
	    if (datum.getClassIndex() == 1) {
		w++;
	    }
	}
	double p = 1.0 * w / n;
	System.out.printf("#games=%d, home-losses=%d, home-wins=%d, P(home-win)=%5.3f, P(home-loss)=%5.3f%n", n, n-w, w, p, 1-p);
	System.out.printf("Expected parameter=%f%n", Math.log((1-p) / p));
	GoldData testingData = getMatchData(manager.getMatchesByYear(2012, 2013));
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
		Team homeTeam = match.getFixture().getHomeTeam();
		Location homeLoc = TeamHomeLocation.getTrainingLocation(homeTeam);
		Location ground = match.getFixture().getLocation();
		double homeTravel = (homeLoc == ground) ? 1 : 0;
		return VectorFactory.newVector(1., homeTravel);
	    }
	};
    }

}
