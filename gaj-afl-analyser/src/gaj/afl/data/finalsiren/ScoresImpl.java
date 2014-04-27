package gaj.afl.data.finalsiren;

import gaj.afl.data.match.Quarter;
import gaj.afl.data.match.Score;
import gaj.afl.data.match.Scores;

/**
 * Encapsulates the total scores at the end of each quarter.
 */
/*package-private*/ class ScoresImpl implements Scores {

	/** Total scores per quarter - assumes ordinal ordering. */
	private final Score[] scores;
	
	/*package-private*/ ScoresImpl(Score... scores) {
		this.scores = scores;
	}

	@Override
	public Score getRelativeScore(Quarter quarter) {
		final int idx = quarter.ordinal();
		return (idx == 0) ? scores[0] : diff(scores[idx-1], scores[idx]);
	}

	private Score diff(Score prevScore, Score curScore) {
		return new MutableScoreImpl(
				curScore.numGoals() - prevScore.numGoals(), 
				curScore.numBehinds() - prevScore.numBehinds()); 
	}

	@Override
	public Score getTotalScore(Quarter quarter) {
		return scores[quarter.ordinal()];
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("{ ");
		final int end = Quarter.values().length - 1;
		for (int i = 0; i <= end; i++) {
			buf.append(Quarter.values()[i].toString());
			buf.append(": ");
			buf.append(scores[i].numGoals());
			buf.append(".");
			buf.append(scores[i].numBehinds());
			buf.append((i == end) ? " }" : ", ");
		}
		return buf.toString();
	}

}
