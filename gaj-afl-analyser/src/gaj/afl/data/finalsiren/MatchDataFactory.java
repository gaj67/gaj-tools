package gaj.afl.data.finalsiren;

import gaj.afl.data.store.MatchFetcher;

/**
 * Provides access to Final Siren match data.
 */
public abstract class MatchDataFactory {

    private MatchDataFactory() {
    }

    /**
     * @return A manager for obtaining historical match data.
     */
    public static MatchFetcher newMatchFetcher() {
        return new MatchDataScraper();
    }
}
