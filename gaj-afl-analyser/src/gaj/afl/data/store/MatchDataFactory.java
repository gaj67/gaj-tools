package gaj.afl.data.store;

import gaj.afl.data.core.MatchFetcher;

/**
 * Provides access to historical match data.
 */
public abstract class MatchDataFactory {

    private MatchDataFactory() {}

    /**
     * @return A manager for obtaining historical match data.
     */
    public static MatchFetcher getMatchFetcher() {
	return gaj.afl.data.finalsiren.MatchDataFactory.newMatchFetcher();
    }
}
