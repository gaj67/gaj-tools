package gaj.afl.datatype;

/**
 * Encapsulates the four quarter scores of a match for a
 * single team.
 */
public interface Scores {

	/**
	 * Obtains the non-cumulative score at the end of
	 * the given quarter, relative to the start of the quarter.
	 * 
	 * @param quarter - The quarter of play.
	 * @return The score just for that quarter.
	 */
	public Score getRelativeScore(Quarter quarter);
	
	/**
	 * Obtains the cumulative score at the end of the
	 * given quarter, from the start of the match.
	 * 
	 * @param quarter - The quarter of play.
	 * @return The total score.
	 */
	public Score getTotalScore(Quarter quarter);

}
