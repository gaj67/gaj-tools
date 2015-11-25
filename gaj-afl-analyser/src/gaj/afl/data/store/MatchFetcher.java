package gaj.afl.data.store;

import gaj.afl.data.core.Match;
import gaj.afl.data.core.Round;
import gaj.afl.data.core.Team;
import java.util.Collection;

/**
 * An interface for obtaining historical match records.
 */
public interface MatchFetcher {

    /**
     * Obtains the historical match records for the given years.
     *
     * @param years - An optional array of years. If specified, then
     * the match records returned are restricted to these years;
     * otherwise all records are returned.
     * @return A collection of match records, which will contain
     * duplications if the same year is repeated.
     */
    public Collection<Match> getMatches(int... years);

    /**
     * Obtains the historical match records for a given team for one or more given years.
     *
     * @param team - The (home or away) team.
     * @param years - An optional array of years. If specified, then
     * the team match records returned are restricted to these years;
     * otherwise all team match records are returned.
     * @return The matches.
     */
    public Collection<Match> getMatches(Team team, int... years);

    /**
     * Obtains the historical match record for a given team for a given round and year.
     *
     * @param team - The (home or away) team.
     * @param year - The match year.
     * @param round - The match round of the year.
     * @return The match, or a value of null if the match could not be found (e.g. the team had a bye).
     */
    public/* @Nullable */Match getMatch(Team team, int year, Round round);

}