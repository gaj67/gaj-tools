package gaj.afl.data.finalsiren;

import java.util.Date;

import gaj.afl.datatype.Fixture;
import gaj.afl.datatype.Location;
import gaj.afl.datatype.Round;
import gaj.afl.datatype.Team;

/*package-private*/ class FixtureImpl implements Fixture {

	private final Round round;
	private final Date datetime;
	private final Location location;
	private final Team homeTeam;
	private final Team awayTeam;

	/*package-private*/  FixtureImpl(Round round, Date datetime, Location location,
			Team homeTeam, Team awayTeam) {
		this.round = round;
		this.datetime = datetime;
		this.location = location;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
	}

	@Override
	public Round getRound() {
		return round;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public Date getDateTime() {
		return (Date) datetime.clone();
	}

	@Override
	public Team getHomeTeam() {
		return homeTeam;
	}

	@Override
	public Team getAwayTeam() {
		return awayTeam;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("{ ");
		buf.append(round.toString());
		buf.append(", ");
		buf.append(location.toString());
		buf.append(", ");
		buf.append(datetime.toString());
		buf.append(", ");
		buf.append("Home-team: ");
		buf.append(homeTeam.toString());
		buf.append(", ");
		buf.append("Away-team: ");
		buf.append(awayTeam.toString());
		buf.append(" }");
		return buf.toString();
	}
}
