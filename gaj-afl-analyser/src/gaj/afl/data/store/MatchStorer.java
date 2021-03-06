package gaj.afl.data.store;

import gaj.afl.data.core.Match;
import java.util.Collection;

/**
 * An interface for storing historical match records.
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