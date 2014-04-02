package gaj.afl.data.match;

/**
 * Defines the score of a single team at some point in time,
 * either a partial score (e.g. for each quarter) or a total
 * score (e.g. at the end of a match).
 */
public interface Score {

	/**
	 * @return The number of goals scored.
	 */
	public int numGoals();

	/**
	 * @return The number of behinds scored.
	 */
	public int numBehinds();

	/**
	 * @return The number of points scored.
	 */
	public int numPoints();

}