package gaj.data.classifier;

import gaj.data.numeric.DataVector;

public class LogProbScorer extends AbstractScorer {

	protected LogProbScorer(GoldData goldStandard) {
		super(goldStandard);
	}

	@Override
	protected double score(DataVector probs, int classIndex) {
		return Math.log(probs.get(classIndex));
	}

}
