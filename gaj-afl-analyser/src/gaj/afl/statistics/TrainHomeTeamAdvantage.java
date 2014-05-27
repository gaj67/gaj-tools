package gaj.afl.statistics;
import gaj.afl.classifier.LoggedClassifierTrainer;
import gaj.afl.data.MatchDataFactory;
import gaj.afl.data.classifier.GoldMatchDataNoDraws;
import gaj.afl.data.match.Match;
import gaj.afl.data.match.MatchFetcher;
import gaj.analysis.classifier.ClassifierFactory;
import gaj.analysis.vector.VectorFactory;
import gaj.data.classifier.GoldData;
import gaj.data.classifier.GoldDatum;
import gaj.data.vector.DataVector;

import java.util.Collection;


/**
 * Tests for home-ground advantage by training a classifier with a constant feature value of 1.
 */
public class TrainHomeTeamAdvantage {

	public static void main(String[] args) {
		// Collect all match statistics...
		MatchFetcher manager = MatchDataFactory.newManager();
		GoldData trainingData = getMatchData(manager.getMatchesByYear(2008, 2009, 2010, 2011));
		int n = 0, w = 0;
		for (GoldDatum datum : trainingData) {
			n++;
			if (datum.getClassIndex() == 1) w++;
		}
		double p = 1.0 * w / n;
		System.out.printf("#games=%d, home-losses=%d, home-wins=%d, P(home-win)=%5.3f, P(home-loss)=%5.3f%n", n, n-w, w, p, 1-p);
		System.out.printf("Expected parameter=%f%n", Math.log((1-p) / p));
		GoldData testingData = getMatchData(manager.getMatchesByYear(2012, 2013));
		getTrainer(trainingData, testingData).train(false, 10);
		getTrainer(trainingData, testingData).train(true, 1);
	}

	private static LoggedClassifierTrainer getTrainer(GoldData trainingData,
			GoldData testingData) {
		LoggedClassifierTrainer trainer = new LoggedClassifierTrainer(
				ClassifierFactory.newTrainableClassifier(trainingData.numClasses(), trainingData.numFeatures()), 
				trainingData, testingData);
		return trainer;
	}

	private static GoldData getMatchData(final Collection<Match> matches) {
		return new GoldMatchDataNoDraws(matches) {
			private final DataVector FEATURES = VectorFactory.newSparseVector(1, 0, 1.);
			@Override
			public int numFeatures() {
				return 1;
			}

			@Override
			protected DataVector getFeatures(Match match) {
				return FEATURES;
			}
		};
	}

}
