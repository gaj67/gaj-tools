package gaj.afl.data;

import gaj.afl.data.manager.MatchFetchingManager;

/**
 * Provides access to historical match data.
 */
public abstract class DataFactory {

	private DataFactory() {}

	/**
	 * @return A manager for obtaining historical match data.
	 */
	public static MatchFetchingManager newManager() {
		return gaj.afl.data.finalsiren.DataFactory.newManager();
	}
}
