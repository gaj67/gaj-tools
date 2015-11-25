package gaj.afl.data;

import gaj.afl.data.store.MatchFetcher;

/**
 * Provides access to historical match data.
 */
public abstract class MatchDataFactory {

    private MatchDataFactory() {
    }

    /**
     * @return A manager for obtaining historical match data.
     */
    public static MatchFetcher getMatchFetcher() {
        return gaj.afl.data.finalsiren.MatchDataFactory.newMatchFetcher();
    }

}
