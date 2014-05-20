package gaj.afl.statistics;

import gaj.afl.data.match.Location;
import gaj.afl.data.match.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Specifies the home training ground of each team.
 */
public class TeamHomeLocation {

	private static final Map<Team, Location> TEAM_LOCATION = new HashMap<Team, Location>() {
		private static final long serialVersionUID = 1L;
		{
			put(Team.Adelaide, Location.Football_Park);
			put(Team.Brisbane_Lions, Location.Gabba);
			put(Team.Carlton, Location.Princes_Park);
			put(Team.Collingwood, Location.Olympic_Park_Oval);
			put(Team.Essendon, Location.Melbourne_Airport_Training_Facility);
			put(Team.Fremantle, Location.Fremantle_Oval);
			put(Team.Geelong, Location.Kardinia_Park);
			put(Team.Gold_Coast, Location.Carrara);
			put(Team.GWS_Giants, Location.Tom_Wills_Oval);
			put(Team.Hawthorn, Location.Waverley_Park);
			put(Team.Melbourne, Location.Gosch__s_Paddock);
			put(Team.North_Melbourne, Location.Arden_Street_Oval);
			put(Team.Port_Adelaide, Location.Alberton_Oval);
			put(Team.Richmond, Location.Punt_Road_Oval);
			put(Team.St_Kilda, Location.Linen_House_Centre); // Moved 2009-10?
			put(Team.Sydney, Location.Sydney_Cricket_Ground);
			put(Team.West_Coast, Location.Subiaco_Oval);
			put(Team.Western_Bulldogs, Location.Whitten_Oval);
		}
	};
	
	// TODO Use year argument to allow for teams moving.
	/**
	 * Obtains the current location at which the given team trains.
	 *  
	 * @param team - The team.
	 * @return The training location.
	 */
	public static Location getTrainingLocation(Team team) {
		Location loc = TEAM_LOCATION.get(team);
		Objects.requireNonNull(loc, "Cannot find a training location for team: " + team);
		return loc;
	}
}
