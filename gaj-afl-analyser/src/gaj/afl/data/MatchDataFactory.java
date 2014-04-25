package gaj.afl.data;

import gaj.afl.data.match.MatchFetcher;

/**
 * Provides access to historical match data.
 */
public abstract class MatchDataFactory {

	private MatchDataFactory() {}

	/**
	 * @return A manager for obtaining historical match data.
	 */
	public static MatchFetcher newManager() {
		return gaj.afl.data.finalsiren.DataFactory.newManager();
	}
}
