package gaj.afl.datatype;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The location (ground) of a match.
 */
public enum MatchLocation {

	AAMI_Stadium, ANZ_Stadium, Aurora_Stadium, Etihad_Stadium, Gabba, Gold_Coast_Stadium,
	Manuka_Oval, Melbourne_Cricket_Ground, Skilled_Stadium,
	Subiaco_Oval, Sydney_Cricket_Ground, TIO_Stadium;

	public static MatchLocation fromString(String value) {
		MatchLocation loc = LOCATIONS.get(value);
		if (loc == null)
			throw new IllegalArgumentException("Unknown location: " + value);
		return loc;
	}

	public String toString() {
		return super.name().replace('_', ' ');
	}

	@SuppressWarnings("serial")
	private static Map<String, MatchLocation> LOCATIONS = new HashMap<String, MatchLocation>() {
		{   // 
			put("Telstra Dome", MatchLocation.Etihad_Stadium);
			put("Telstra_Dome", MatchLocation.Etihad_Stadium);
		}
	};
	static {
		for (MatchLocation loc : values()) {
			LOCATIONS.put(loc.toString(), loc);
			LOCATIONS.put(loc.name(), loc);
		}
	}

	public static void main(String[] args) throws IOException {
		System.out.printf("Locations=\n%s\n", MatchLocation.LOCATIONS);
	}

}
