package gaj.analysis.classifier;

import gaj.analysis.matrix.MatrixFactory;
import gaj.analysis.vector.VectorFactory;
import gaj.data.classifier.DatumScore;
import gaj.data.matrix.DataMatrix;
import gaj.data.matrix.WritableMatrix;
import gaj.data.vector.DataVector;
import gaj.data.vector.WritableVector;

public class LogisticClassifier extends BaseClassifier<DataMatrix> {

	private final int Cm1;
	/*
	 * Model parameters, V = [V_c], such that
	 *    P(c|x) = e^{V_c.x} / sum{c'} e^{V_c'.x}.
	 */
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
	public DataMatrix getParameters() {
		return params;
	}

	@Override
	public boolean setParameters(DataMatrix params) {
		this.params.set(params);
		return true;
	}

	@Override
	public boolean hasGradient() {
		return true;
	}

	/* 
	 * Computes ds/dV = [ ds/dV_c' ],
	 * where 
	 * 	  ds/dV_c' = [ dP(c|x)/dV_c' ] * [ ds/dP(c|x) ]
	 *             = dyad{[w{c,c'}], x}  * [ds/dP(c|x)]
	 *             = ( [w{c,c'}] . [ds/dP(c|x)] ) x
	 * and
	 *    w{c,c'} = delta{c,c'}P(c'|x) - P(c|x)P(c'|x). 
	 */
	@Override
	public DataMatrix getGradient(DatumScore datumScore) {
		WritableMatrix gradient = MatrixFactory.newWritableMatrix(Cm1, numFeatures);
		DataVector probs = datumScore.getPosteriors();
		for (int cPrime = 0; cPrime < Cm1; cPrime++) {
			WritableVector w = VectorFactory.newWritableVector(numClasses);
			w.set(VectorFactory.scale(probs, -probs.get(cPrime)));
			w.add(cPrime, probs.get(cPrime));
			double classWeight = VectorFactory.dot(w, datumScore.getGradient());
			DataVector featureWeights = VectorFactory.scale(datumScore.getFeatures(), classWeight);
			gradient.setRow(cPrime, featureWeights);
		}
		return gradient;
	}

}
