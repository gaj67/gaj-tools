package gaj.afl.statistics;
import gaj.afl.data.MatchDataFactory;
import gaj.afl.data.classifier.GoldMatchDataNoDraws;
import gaj.afl.data.match.Location;
import gaj.afl.data.match.Match;
import gaj.afl.data.match.MatchFetcher;
import gaj.afl.data.match.Team;
import gaj.analysis.classifier.AccuracyScorer;
import gaj.analysis.classifier.ClassifierFactory;
import gaj.analysis.classifier.LogProbScorer;
import gaj.analysis.numeric.NumericFactory;
import gaj.analysis.vector.VectorFactory;
import gaj.data.classifier.DataScorer;
import gaj.data.classifier.GoldData;
import gaj.data.classifier.GoldDatum;
import gaj.data.classifier.ScoredTrainer;
import gaj.data.classifier.TrainableClassifier;
import gaj.data.classifier.TrainingControl;
import gaj.data.classifier.TrainingState;
import gaj.data.classifier.TrainingSummary;
import gaj.data.vector.DataVector;

import java.util.Collection;


/**
 * Tests for home-ground advantage by training a classifier with a constant feature value of 1.
 */
public class TrainHomeTeamAdvantage2Detailed {

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
		DataScorer[] scorers = new DataScorer[] {
			new LogProbScorer(trainingData),
			new AccuracyScorer(trainingData, 1e-3),
			new LogProbScorer(testingData),
			new AccuracyScorer(testingData, 1e-3),
		};
		//train(false, scorers);
		train(true, scorers);
	}

	private static void train(boolean useAcceleration, DataScorer[] scorers) {
		System.out.printf("Using acceleration: %s%n", useAcceleration);
		long start = System.currentTimeMillis();
		int numClasses = scorers[0].numClasses();
		int numFeatures = scorers[0].numFeatures();
		TrainableClassifier classifier = ClassifierFactory.newDefaultClassifier(numClasses, numFeatures);
		TrainingControl control = getControl(useAcceleration);
		ScoredTrainer trainer = classifier.getTrainer(scorers);
		System.out.println("Iteration, scores");
		printScores("" + trainer.numIterations(), trainer.getScores());
		TrainingState state = TrainingState.NOT_HALTED;
		while (state != TrainingState.SCORE_CONVERGED) {
			TrainingSummary summary = trainer.train(control);
			printScores("" + trainer.numIterations(), trainer.getScores());
			state = summary.getTrainingState();
			if (state != TrainingState.MAX_ITERATIONS_EXCEEDED) break; // What happened?
		}
		System.out.printf("Final state=%s%n", state);
		long end = System.currentTimeMillis();
		double time = 1e-3 * (end - start);
		System.out.printf("#iterations=%d, time=%4.2f seconds (%4.2f ms/iter)%n", 
				trainer.numIterations(), time, 1e3 * time / trainer.numIterations());
		NumericFactory.display("Final classifier parameters =", classifier.getParameters());
	}

	private static void printScores(String label, double[] scores) {
		System.out.printf("%s [", label);
		for (double score : scores)
			System.out.printf(" %f", score);
		System.out.println(" ]");
	}

	private static TrainingControl getControl(final boolean useAcceleration) {
		return new TrainingControl() {
			@Override
			public double scoreTolerance() {
				return 1e-14;
			}
			
			@Override
			public int maxIterations() {
				return 1;
			}

			@Override
			public int maxSubIterations() {
				return 10;
			}
			
			@Override
			public double gradientTolerance() {
				return 0;
			}

			@Override
			public double relativeScoreTolerance() {
				return 0;
			}

			@Override
			public boolean useAcceleration() {
				return useAcceleration;
			}
		};
	}

	private static GoldData getMatchData(final Collection<Match> matches) {
		return new GoldMatchDataNoDraws(matches) {
			@Override
			public int numFeatures() {
				return 3;
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
				return VectorFactory.newVector(1., homeDist, awayDist);
			}
		};
	}

	private static double computeDistance(Location loc1, Location loc2) {
		double x = (loc1.getLongitude() - loc2.getLongitude());
		double y = (loc1.getLatitude() - loc2.getLatitude());
		return Math.sqrt(x * x + y * y);
	}
}
