package gaj.analysis.classifier;

import gaj.analysis.numeric.NumericDataFactory;
import gaj.data.classifier.GoldData;
import gaj.data.numeric.DataVector;

/**
 * Implements a log-likelihood scoring function:
 * <pre><tt>s_d = ln P(c_d|x_d)</tt></pre>
 * where <tt>c_d</tt> is the index of the true class 
 * of <tt>d</tt>-th feature vector <tt>x_d</tt>.
 * <p/>The score gradient is also computed as:
 * <pre><tt>g_d,c = delta(c,c_d) / P(c_d|x_d)</tt></pre>
 * where <tt>delta(c,c_d)=1</tt> if and only if
 * <tt>c_d=c</tt> (otherwise it is 0).
 */
public class LogProbScorer extends AverageScorer {

	protected LogProbScorer(GoldData goldStandard) {
		super(goldStandard);
	}

	@Override
	protected double score(DataVector probs, int classIndex) {
		return Math.log(probs.get(classIndex));
	}

	@Override
	public boolean hasGradient() {
		return true;
	}

	@Override
	protected DataVector gradient(DataVector probs, int classIndex) {
		return NumericDataFactory.newSparseVector(numClasses(), classIndex, 1. / probs.get(classIndex));
	}

}
