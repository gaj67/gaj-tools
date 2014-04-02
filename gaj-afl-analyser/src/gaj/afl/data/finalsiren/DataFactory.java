package gaj.afl.data.finalsiren;

import gaj.afl.data.manager.MatchFetchingManager;

/**
 * Provides access to Final Siren match data.
 */
public abstract class DataFactory {

	private DataFactory() {}

	/**
	 * @return A manager for obtaining historical match data.
	 */
	public static MatchFetchingManager newManager() {
		return new DataScraper();
	}
}
