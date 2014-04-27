package gaj.afl.data.finalsiren;

import gaj.afl.data.match.MatchFetcher;

/**
 * Provides access to Final Siren match data.
 */
public abstract class DataFactory {

	private DataFactory() {}

	/**
	 * @return A manager for obtaining historical match data.
	 */
	public static MatchFetcher newManager() {
		return new DataScraper();
	}
}
