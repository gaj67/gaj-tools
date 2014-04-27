package gaj.afl.data.match;


import java.util.Collection;

/**
 * Describes the interface for storing historical match records.
 */
public interface MatchStorer {

	/**
	 * Stores the given match records.
	 * 
	 * @param matches - A collection of match records.
	 */
	public void putMatches(Collection<Match> matches);

	/**
	 * Stores the given match record.
	 * 
	 * @param match - The match record.
	 */
	public void putMatch(Match match);

}