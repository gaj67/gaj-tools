package gaj.afl.statistics;
import gaj.afl.data.MatchDataFactory;
import gaj.afl.data.classifier.GoldMatchDataNoDraws;
import gaj.afl.data.match.Match;
import gaj.afl.data.match.MatchFetcher;
import gaj.analysis.classifier.ClassifierFactory;
import gaj.analysis.classifier.LogProbScorer;
import gaj.analysis.numeric.NumericFactory;
import gaj.analysis.vector.VectorFactory;
import gaj.data.classifier.DataScorer;
import gaj.data.classifier.GoldData;
import gaj.data.classifier.GoldDatum;
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
		int n = 0, w = 0;
		for (GoldDatum datum : trainingData) {
			n++;
			if (datum.getClassIndex() == 1) w++;
		}
		System.out.printf("n=%d, l=%d, w=%d%n", n, n-w, w);
		System.out.printf("Proportions=[%4.2f, %4.2f]%n", 1.0*(n-w)/n, 1.0*w/n);
		System.out.printf("Expected parameter=%f%n", Math.log(1.0*(n-w)/w));
		GoldData testingData = getMatchData(manager.getMatchesByYear(2012));
		DataScorer[] scorers = new DataScorer[] {
			new LogProbScorer(trainingData),
			new LogProbScorer(testingData)
		};
		int numClasses = scorers[0].numClasses();
		int numFeatures = scorers[0].numFeatures();
		TrainableClassifier classifier = ClassifierFactory.newDefaultClassifier(numClasses, numFeatures);
		TrainingParams control = getControl();
		TrainingSummary summary = classifier.getTrainer(scorers).train(control);
		System.out.printf("#iterations=%d%n", summary.numIterations());
		printScores("Initial", summary.initalScores());
		printScores("Final", summary.finalScores());
		NumericFactory.display("Final classifier parameter=", classifier.getParameters());
	}

	private static void printScores(String label, double[] scores) {
		System.out.printf("%s scores = [", label);
		for (double score : scores)
			System.out.printf(" %f", score);
		System.out.println(" ]");
	}

	private static TrainingParams getControl() {
		return new TrainingParams() {
			@Override
			public double scoreTolerance() {
				return 1e-14;
			}
			
			@Override
			public int maxIterations() {
				return 0;
			}
			
			@Override
			public double gradientTolerance() {
				return 0;
			}

			@Override
			public double relativeScoreTolerance() {
				return 0;
			}
		};
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
