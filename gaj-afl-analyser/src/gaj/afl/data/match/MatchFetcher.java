package gaj.afl.data.match;


import java.util.Collection;

/**
 * Describes the interface for obtaining historical match records.
 */
public interface MatchFetcher {

	/**
	 * Obtains all historical match records.
	 * 
	 * @return A collection of match records.
	 */
	public Collection<Match> getMatches();

	/**
	 * Obtains the historical match records for the given years.
	 * 
	 * @param years - An optional array of years. If specified, then
	 * the match records returned are restricted to these years; 
	 * otherwise no records are returned.
	 * @return A collection of match records, which will contain
	 * duplications if the same year is repeated.
	 */
	public Collection<Match> getMatchesByYear(int... years);

}