package gaj.afl.data.finalsiren;

import gaj.afl.data.match.Team;
import java.io.File;

/**
 * Provides mappings between match data objects and identifiers, e.g. file names.
 */
/*package-private*/ abstract class MatchDataIdentifiers {

    private static final File PATH_TO_DATA = new File("data/finalsiren/match/");
    private static final Team[] teams = new Team[] {
	Team.Adelaide, Team.Brisbane_Lions, Team.Collingwood, Team.Geelong,

    };

    private MatchDataIdentifiers() {}

    /**
     * Obtains a globally unique identifier for a team.
     *
     * @param team - The team.
     * @return The team identifier.
     */
    /*package-private*/ static int getTeamIdentifier(Team team) {
	for (int i = 0; i < teams.length; i++) {
	    if (teams[i] == team) {
		return i + 1;
	    }
	}
	throw new IllegalArgumentException("Could not find identifier for team: " + team);
    }

    /**
     * Obtains a globally unique identifier string for a team.
     *
     * @param team - The team.
     * @return The team identifier string.
     */
    /*package-private*/ static String getTeamIdentifierString(Team team) {
	return "Team" + getTeamIdentifier(team);
    }

    /**
     * Obtains a globally unique identifier string for a team.
     *
     * @param teamId - The team identifier.
     * @return The team identifier string.
     */
    /*package-private*/ static String getTeamIdentifierString(int teamId) {
	return "Team" + teamId;
    }

    /*package-private*/ static Team getTeamFromIdentifier(int id) {
	if (id < 1 || id > teams.length) {
	    throw new IllegalArgumentException("No such team identifier: " + id);
	}
	return teams[id - 1];
    }

    /**
     * Obtains a global identifier to designate all matches for a given team in a given year.
     *
     * @param team - The team.
     * @param year - The match year.
     * @return The team-year matches identifier.
     */
    /*package-private*/ static String getTeamYearIdentifierString(Team team, int year) {
	return "" + year + "-" + getTeamIdentifierString(team);
    }

    /**
     * Obtains a global identifier to designate all matches for a given team in a given year.
     *
     * @param team - The team identifier.
     * @param year - The match year.
     * @return The team-year matches identifier.
     */
    /*package-private*/ static String getTeamYearIdentifierString(int teamId, int year) {
	return "" + year + "-" + getTeamIdentifierString(teamId);
    }

    /*package-private*/ static File getMatchDataPath() {
	return PATH_TO_DATA;
    }

    /*package-private*/ static File getMatchDataPath(int year) {
	return new File(PATH_TO_DATA, "" + year);
    }

    /**
     * Obtains the path to the file containing all matches for a given team in a given year.
     *
     * @param team - The team.
     * @param year - The match year.
     * @return The team-year matches file path.
     */
    /*package-private*/ static File getMatchDataPath(Team team, int year) {
	return getMatchDataPath(getTeamIdentifier(team), year);
    }

    /**
     * Obtains the path to the file containing all matches for a given team in a given year.
     *
     * @param team - The team identifier.
     * @param year - The match year.
     * @return The team-year matches file path.
     */
    /*package-private*/ static File getMatchDataPath(int teamId, int year) {
	return new File(getMatchDataPath(year), getTeamYearIdentifierString(teamId, year) + ".htm");
    }

}
