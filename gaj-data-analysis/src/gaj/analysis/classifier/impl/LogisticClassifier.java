package gaj.analysis.classifier.impl;

import gaj.analysis.classifier.DatumScore;
import gaj.analysis.classifier.updated.Classification;
import gaj.analysis.numeric.matrix.WritableMatrix;
import gaj.analysis.numeric.matrix.impl.MatrixFactory;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.WritableVector;
import gaj.analysis.numeric.vector.impl.VectorFactory;

public class LogisticClassifier extends BaseClassifier {

    private final int Cm1;
    /*
     * Model parameters, V = [V_c], such that
     * P(c|x) = e^{V_c.x} / sum{c'} e^{V_c'.x}.
     */
    private final WritableMatrix matParams;
    private final WritableVector vecParams;

    public LogisticClassifier(int numClasses, int numFeatures) {
        super(numClasses, numFeatures);
        Cm1 = numClasses - 1;
        matParams = MatrixFactory.newMatrix(Cm1, numFeatures);
        vecParams = MatrixFactory.asVector(matParams);
    }

    @Override
    public Classification classify(DataVector features) {
        DataVector weights = MatrixFactory.multiply(matParams, features);
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
        return new ClassificationImpl(features, VectorFactory.newVector(posteriors));
    }

    @Override
    public DataVector getParameters() {
        return vecParams;
    }

    @Override
    public boolean setParameters(DataVector params) {
        vecParams.set(params);
        return true;
    }

    @Override
    public boolean hasGradient() {
        return true;
    }

    /*
     * Computes ds/dV = [ ds/dV_c' ],
     * where
     * ds/dV_c' = [ dP(c|x)/dV_c' ] * [ ds/dP(c|x) ]
     * = dyad{[w{c,c'}], x} * [ds/dP(c|x)]
     * = ( [w{c,c'}] . [ds/dP(c|x)] ) x
     * and
     * w{c,c'} = delta{c,c'}P(c'|x) - P(c|x)P(c'|x).
     */
    @Override
    public DataVector getGradient(DatumScore datumScore) {
        WritableMatrix gradient = MatrixFactory.newMatrix(Cm1, numFeatures);
        DataVector probs = datumScore.getPosteriors();
        for (int cPrime = 0; cPrime < Cm1; cPrime++) {
            WritableVector w = VectorFactory.newVector(numClasses);
            w.set(VectorFactory.scale(probs, -probs.get(cPrime)));
            w.add(cPrime, probs.get(cPrime));
            double classWeight = VectorFactory.dot(w, datumScore.getGradient());
            DataVector featureWeights = VectorFactory.scale(datumScore.getFeatures(), classWeight);
            gradient.setRow(cPrime, featureWeights);
        }
        return MatrixFactory.asVector(gradient);
    }

    @Override
    public int numParameters() {
        return vecParams.size();
    }

}
