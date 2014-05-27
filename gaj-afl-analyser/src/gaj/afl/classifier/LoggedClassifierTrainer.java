package gaj.afl.classifier;
import gaj.analysis.classifier.AccuracyScorer;
import gaj.analysis.classifier.LogProbScorer;
import gaj.analysis.numeric.NumericFactory;
import gaj.data.classifier.DataScorer;
import gaj.data.classifier.GoldData;
import gaj.data.classifier.ScoredTrainer;
import gaj.data.classifier.TrainableClassifier;
import gaj.data.classifier.TrainingControl;
import gaj.data.classifier.TrainingState;
import gaj.data.classifier.TrainingSummary;


/**
 * Offers a mechanism for training a given classifier on given data, using gradient ascent.
 * The iteration scores can optionally be traced for each major iteration.
 */
public class LoggedClassifierTrainer {

	private final TrainableClassifier classifier;
	private final GoldData trainingData;
	private final GoldData testingData;

	public LoggedClassifierTrainer(TrainableClassifier classifier, GoldData trainingData, GoldData testingData) {
		this.classifier = classifier;
		this.trainingData = trainingData;
		this.testingData = testingData;
		
	}

	public void train(boolean useAcceleration, boolean showScoreTrace) {
		System.out.printf("Using acceleration: %s, Showing score trace: %s%n", useAcceleration, showScoreTrace);
		TrainingControl control = getControl(useAcceleration, showScoreTrace ? 1 : 0);
		train(classifier.getTrainer(getScorers()), control);
	}

	protected DataScorer[] getScorers() {
		DataScorer[] scorers = new DataScorer[] {
			new LogProbScorer(trainingData),
			new AccuracyScorer(trainingData, 1e-3),
			new LogProbScorer(testingData),
			new AccuracyScorer(testingData, 1e-3),
		};
		return scorers;
	}

	protected void train(ScoredTrainer trainer, TrainingControl control) {
		long start = System.currentTimeMillis();
		System.out.println("Iteration, scores");
		printScores("" + trainer.numIterations(), trainer.getScores());
		TrainingState state = TrainingState.NOT_HALTED;
		while (state != TrainingState.SCORE_CONVERGED) {
			TrainingSummary summary = trainer.train(control);
			printScores("" + trainer.numIterations() + ", ", trainer.getScores());
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

	protected static TrainingControl getControl(final boolean useAcceleration, final int maxIterations) {
		return new TrainingControl() {
			@Override
			public double scoreTolerance() {
				return 1e-14;
			}
			
			@Override
			public int maxIterations() {
				return maxIterations;
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
}
