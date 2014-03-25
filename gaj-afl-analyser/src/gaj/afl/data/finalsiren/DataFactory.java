package gaj.afl.data.finalsiren;

import gaj.afl.data.DataManager;

/**
 * Provides access to Final Siren match data.
 */
public abstract class DataFactory {

	private DataFactory() {}

	/**
	 * @return A manager for obtaining historical match data.
	 */
	public static DataManager newManager() {
		return new DataScraper();
	}
}
