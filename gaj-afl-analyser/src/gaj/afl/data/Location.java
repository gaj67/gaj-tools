package gaj.afl.data;

/**
 * Specifies the location (ground) of a match.
 */
public enum Location {

	AAMI_Stadium, ANZ_Stadium, Aurora_Stadium, 
	Etihad_Stadium, Gabba, Gold_Coast_Stadium,
	Manuka_Oval, Melbourne_Cricket_Ground, Skilled_Stadium,
	Subiaco_Oval, Sydney_Cricket_Ground, TIO_Stadium, 
	Metricon_Stadium, Patersons_Stadium, Adelaide_Oval,
	Cazaly__s_Stadium, Skoda_Stadium, Blacktown_ISP,
	Blundstone_Arena, Football_Park, Telstra_Dome,
	W___A___C___A___, Colonial_Stadium, Optus_Oval,
	Shell_Stadium, York_Park, Simonds_Stadium,
	Stadium_Australia, Telstra_Stadium, Westpac_Stadium,
	Marrara, Carrara,
	;

	/**
	 * Obtains the type from an external string representation.
	 * 
	 * @param location - The string representation.
	 * @return The match location.
	 */
	public static Location fromExternal(String location) {
		return Location.valueOf(toInternal(location));
	}

	private static String toInternal(String location) {
		return location.replace(' ', '_').replace("'", "__").replace(".", "___");
	}

	/**
	 * Obtains an external string representation of the type.
	 * 
	 * @return The string representation.
	 */
	public String toExternal() {
		return name().replace("___", ".").replace("__", "'").replace('_', ' ');
	}

	public String toString() {
		return toExternal();
	}

}
