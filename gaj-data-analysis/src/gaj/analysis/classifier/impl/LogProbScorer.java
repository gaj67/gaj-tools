package gaj.analysis.classifier.impl;

import gaj.analysis.classifier.GoldData;
import gaj.analysis.data.vector.DataVector;
import gaj.analysis.data.vector.impl.VectorFactory;

/**
 * Implements a log-likelihood scoring function:
 * 
 * <pre>
 * <tt>s_d = ln P(c_d|x_d)</tt>
 * </pre>
 * 
 * where <tt>c_d</tt> is the index of the true class
 * of <tt>d</tt>-th feature vector <tt>x_d</tt>.
 * <p/>
 * The score gradient is also computed as:
 * 
 * <pre>
 * <tt>g_d,c = delta(c,c_d) / P(c_d|x_d)</tt>
 * </pre>
 * 
 * where <tt>delta(c,c_d)=1</tt> if and only if <tt>c_d=c</tt> (otherwise it is 0).
 */
public class LogProbScorer extends BaseScorer {

    public LogProbScorer(GoldData goldStandard) {
        super(goldStandard);
    }

    @Override
    protected double getScore(DataVector probs, int classIndex) {
        return Math.log(probs.get(classIndex));
    }

    @Override
    public boolean hasGradient() {
        return true;
    }

    @Override
    protected DataVector getGradient(DataVector probs, int classIndex) {
        return VectorFactory.newSparseVector(numClasses(), classIndex, 1. / probs.get(classIndex));
    }

}
