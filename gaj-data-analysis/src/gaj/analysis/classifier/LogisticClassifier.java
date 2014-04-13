package gaj.analysis.classifier;

import gaj.data.classifier.DataScorer;
import gaj.data.classifier.TrainableClassifier;
import gaj.data.classifier.TrainingParams;
import gaj.data.classifier.TrainingSummary;
import gaj.data.numeric.DataVector;
import gaj.data.numeric.NumericDataFactory;

public class LogisticClassifier implements TrainableClassifier {

	private final int numClasses;
	private final int Cm1;
	private final int numFeatures;
	private final DataVector[] params;

	public LogisticClassifier(int numClasses, int numFeatures) {
		this.numClasses = numClasses;
		Cm1 = numClasses - 1;
		this.numFeatures = numFeatures;
		params = new DataVector[numClasses-1];
		for (int c = 0; c < numClasses; c++)
			params[c] = NumericDataFactory.newZeroVector(numFeatures);
	}

	@Override
	public int numClasses() {
		return numClasses;
	}

	@Override
	public int numFeatures() {
		return numFeatures;
	}

	@Override
	public DataVector classify(DataVector features) {
		double[] posteriors = new double[numClasses];
		posteriors[Cm1] = 1;
		double norm = 1;
		for (int c = 0; c < Cm1; c++) {
			double p = Math.exp(NumericDataFactory.dot(params[c], features));
			posteriors[c] = p;
			norm += p;
		}
		norm = 1 / norm;
		for (int c = 0; c < numClasses; c++)
			posteriors[c] *= norm;
		return NumericDataFactory.newDenseVector(posteriors);
	}

	@Override
	public TrainingSummary train(TrainingParams params,
			DataScorer trainingScorer, DataScorer... testingScorers) {
		// TODO Auto-generated method stub
		return null;
	}

}
