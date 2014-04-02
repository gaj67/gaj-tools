package gaj.afl.datatype;

import java.util.Date;

/**
 * Defines the location and date-time of, and 
 * the two teams playing in, a match.
 */
public interface Fixture {

	/**
	 * @return The round of play.
	 */
	public Round getRound();

	/**
	 * @return The location of the match.
	 */
	public Location getLocation();

	/**
	 * @return The date and time of the match.
	 */
	public Date getDateTime();

	/**
	 * @return The team playing on their home ground.
	 */
	public Team getHomeTeam();

	/**
	 * @return The team playing away from their home ground.
	 */
	public Team getAwayTeam();

}
