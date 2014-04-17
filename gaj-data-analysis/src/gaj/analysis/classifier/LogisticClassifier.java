package gaj.analysis.classifier;

import gaj.analysis.numeric.NumericDataFactory;
import gaj.data.numeric.DataMatrix;
import gaj.data.numeric.DataVector;

public class LogisticClassifier extends GradientAscentTrainer {

	private final int Cm1;
	private final DataVector[] params;

	public LogisticClassifier(int numClasses, int numFeatures) {
		super(numClasses, numFeatures, numFeatures*(numClasses-1));
		Cm1 = numClasses - 1;
		params = new DataVector[Cm1];
		for (int c = 0; c < numClasses; c++)
			params[c] = NumericDataFactory.newZeroVector(numFeatures);
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
	protected DataMatrix getGradient(DataVector features) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean update(DataVector deltaParams) {
		for (int i = 0; i < numParameters; i++) {
			int f = i % numFeatures;
			int c = i / numFeatures;
			params
		}
		return true;
	}

}
