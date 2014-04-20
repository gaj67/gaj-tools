package gaj.analysis.classifier;

import gaj.analysis.matrix.MatrixFactory;
import gaj.analysis.vector.VectorFactory;
import gaj.data.classifier.DatumScore;
import gaj.data.matrix.DataMatrix;
import gaj.data.matrix.WritableMatrix;
import gaj.data.numeric.DataObject;
import gaj.data.vector.DataVector;
import gaj.data.vector.WritableVector;

public class LogisticClassifier extends GradientAscentTrainer {

	private final int Cm1;
	private final WritableMatrix params;

	public LogisticClassifier(int numClasses, int numFeatures) {
		super(numClasses, numFeatures);
		Cm1 = numClasses - 1;
		params = MatrixFactory.newWritableMatrix(Cm1, numFeatures);
	}

	@Override
	public DataVector classify(DataVector features) {
		DataVector weights = MatrixFactory.multiply(params, features);
		double[] posteriors = new double[numClasses];
		posteriors[Cm1] = 1;
		double norm = 1;
		for (int c = 0; c < Cm1; c++) {
			double p = Math.exp(weights.get(c));
			posteriors[c] = p;
			norm += p;
		}
		norm = 1 / norm;
		for (int c = 0; c < numClasses; c++)
			posteriors[c] *= norm;
		return VectorFactory.newVector(posteriors);
	}

	@Override
	protected boolean updateParams(DataObject deltaParams) {
		((DataMatrix) deltaParams).addTo(params);
		return true;
	}

	@Override
	protected DataObject getGradient(DatumScore datumScore) {
		WritableMatrix gradient = MatrixFactory.newWritableMatrix(Cm1, numFeatures);
		DataVector probs = datumScore.getPosteriors();
		// classRow = row of param. matrix corresp. to class.
		for (int classRow = 0; classRow < Cm1; classRow++) {
			WritableVector classWeights = VectorFactory.newWritableVector(numClasses);
			classWeights.add(VectorFactory.scale(probs, -probs.get(classRow)));
			classWeights.add(classRow, probs.get(classRow));
			double classWeight = VectorFactory.dot(classWeights, datumScore.getGradient());
			DataVector featureWeights = VectorFactory.scale(datumScore.getFeatures(), classWeight);
			gradient.addRow(classRow, featureWeights);
		}
		return gradient;
	}

}
