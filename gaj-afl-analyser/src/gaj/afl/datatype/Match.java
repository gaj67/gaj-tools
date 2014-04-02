package gaj.afl.datatype;

/**
 * Defines a completed match.
 */
public interface Match {

	/**
	 * @return The match fixture.
	 */
	public Fixture getFixture();
	
	/**
	 * @return The home team's match scores.
	 */
	public Scores getHomeTeamScores();

	/**
	 * @return The away team's match scores.
	 */
	public Scores getAwayTeamScores();

	/**
	 * @return The outcome of the match from the perspective of
	 * the home team.
	 */
	public Outcome getOutcome();
	
}
