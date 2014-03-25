package gaj.afl.data;

import gaj.afl.data.DataManager;

/**
 * Provides access to historical match data.
 */
public abstract class DataFactory {

	private DataFactory() {}

	/**
	 * @return A manager for obtaining historical match data.
	 */
	public static DataManager newManager() {
		return gaj.afl.data.finalsiren.DataFactory.newManager();
	}
}
