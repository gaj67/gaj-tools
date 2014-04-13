package gaj.analysis.classifier;

import gaj.data.classifier.GoldData;
import gaj.data.numeric.DataVector;

public class LogProbScorer extends AverageScorer {

	protected LogProbScorer(GoldData goldStandard) {
		super(goldStandard);
	}

	@Override
	protected double score(DataVector probs, int classIndex) {
		return Math.log(probs.get(classIndex));
	}

}
