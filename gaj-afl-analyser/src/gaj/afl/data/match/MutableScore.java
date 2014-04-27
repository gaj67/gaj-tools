package gaj.afl.data.match;

public interface MutableScore extends Score {

	/**
	 * Adds the given score to the current score.
	 * 
	 * @param score - The incremental score.
	 */
	public void add(Score score);

	/**
	 * Adds the given score to the current score.
	 * 
	 * @param numGoals - The incremental number of goals.
	 * @param numBehinds - The incremental number of behinds.
	 */
	public void add(int numGoals, int numBehinds);

}
