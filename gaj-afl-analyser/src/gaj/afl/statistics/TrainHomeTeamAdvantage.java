package gaj.afl.statistics;
import gaj.afl.data.MatchDataFactory;
import gaj.afl.data.classifier.GoldMatchDataNoDraws;
import gaj.afl.data.match.Match;
import gaj.afl.data.match.MatchFetcher;
import gaj.analysis.classifier.ClassifierFactory;
import gaj.analysis.classifier.LogProbScorer;
import gaj.analysis.vector.VectorFactory;
import gaj.data.classifier.DataScorer;
import gaj.data.classifier.GoldData;
import gaj.data.classifier.TrainableClassifier;
import gaj.data.classifier.TrainingParams;
import gaj.data.classifier.TrainingSummary;
import gaj.data.vector.DataVector;

import java.util.Collection;



public class TrainHomeTeamAdvantage {

	public static void main(String[] args) {
		// Collect all match statistics...
		MatchFetcher manager = MatchDataFactory.newManager();
		GoldData trainingData = getMatchData(manager.getMatchesByYear(2010, 2011));
		GoldData testingData = getMatchData(manager.getMatchesByYear(2012));
		DataScorer[] scorers = new DataScorer[] {
			new LogProbScorer(trainingData),
			new LogProbScorer(testingData)
		};
		int numClasses = scorers[0].numClasses();
		int numFeatures = scorers[0].numFeatures();
		TrainableClassifier classifier = ClassifierFactory.newDefaultClassifier(numClasses, numFeatures);
		TrainingParams control = getControl();
		TrainingSummary summary = classifier.train(control, scorers);
	}

	private static TrainingParams getControl() {
		return new TrainingParams() {
			
			@Override
			public double scoreTolerance() {
				return 0;
			}
			
			@Override
			public int maxIterations() {
				return 10;
			}
			
			@Override
			public double gradientTolerance() {
				return 0;
			}
		};
	}

	private static GoldData getMatchData(final Collection<Match> matches) {
		return new GoldMatchDataNoDraws(matches) {
			private final DataVector FEATURES = VectorFactory.newVector(1, 0, 1.);
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
