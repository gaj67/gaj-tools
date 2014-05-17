package gaj.afl.data.match;

/**
 * Specifies the location (ground) of a match.
 */
public enum Location {

	AAMI_Stadium(0, 138.4936, -34.8798, "Football Park", "West Lakes", "SA"),
	Adelaide_Oval(1, 138.5961, -34.9156, "Adelaide Oval", "Adelaide", "SA"),
	ANZ_Stadium(2, 151.0634, -33.8471, "Stadium Australia", "Sydney Olympic Park", "NSW"), 
	Aurora_Stadium(3, 147.1389, -41.4258, "York Park", "Launceston", "TAS"), 
	Blacktown_ISP(4, 150.8592, -33.7694, "International Sportspark", "Rooty Hill", "NSW"),
	Blundstone_Arena(5, 147.3735, -42.8773, "Bellerive Oval", "Hobart", "TAS"), 
	Carrara(6, 153.3669, -28.0063, "Carrara Stadium", "Gold Coast", "QLD"), 
	Cazaly__s_Stadium(7, 145.7492, -16.9358, "Cazaly's Stadium", "Westcourt", "QLD"),
	Colonial_Stadium(8, 144.9474, -37.8165, "Docklands Stadium", "Melbourne", "VIC"), 
	Etihad_Stadium(8, 144.9474, -37.8165, "Docklands Stadium", "Melbourne", "VIC"),
	Football_Park(0, 138.4936, -34.8798, "Football Park", "West Lakes", "SA"),
	Gabba(9, 153.0381, -27.4859, "Brisbane Cricket Ground", "Brisbane", "QLD"), 
	Gold_Coast_Stadium(6, 153.3669, -28.0063, "Carrara Stadium", "Gold Coast", "QLD"),
	Kadinia_Park(15, 144.3546, -38.1581, "Kardinia Park", "South Geelong", "VIC"), 
	Manuka_Oval(10, 149.1346, -35.3182, "Manuka Oval", "Griffith", "ACT"),
	Marrara(11, 130.8872, -12.3992, "Marrara Stadium", "Darwin", "NT"),
	Melbourne_Cricket_Ground(12, 144.9832 , -37.8200, "Melbourne Cricket Ground", "Melbourne", "VIC"), 
	Metricon_Stadium(6, 153.3669, -28.0063, "Carrara Stadium", "Gold Coast", "QLD"),
	Optus_Oval(13, 144.9617, -37.7839, "Princes Park", "Carlton", "VIC"),
	Patersons_Stadium(14, 115.83, -31.9444, "Subiaco Oval", "Subiaco", "WA"),
	Princes_Park(13, 144.9617, -37.7839, "Princes Park", "Carlton", "VIC"),
	Shell_Stadium(15, 144.3546, -38.1581, "Kardinia Park", "South Geelong", "VIC"), 
	Simonds_Stadium(15, 144.3546, -38.1581, "Kardinia Park", "South Geelong", "VIC"),
	Skilled_Stadium(15, 144.3546, -38.1581, "Kardinia Park", "South Geelong", "VIC"),
	Skoda_Stadium(16, 151.0678, -33.8431, "Sydney Showground Stadium", "Sydney Olympic Park", "NSW"),
	Spotless_Stadium(16, 151.0678, -33.8431, "Sydney Showground Stadium", "Sydney Olympic Park", "NSW"),
	Stadium_Australia(2, 151.0634, -33.8471, "Stadium Australia", "Sydney Olympic Park", "NSW"),
	Subiaco_Oval(14, 115.83, -31.9444, "Subiaco Oval", "Subiaco", "WA"), 
	Sydney_Cricket_Ground(17, 151.2247, -33.8917, "Sydney Cricket Ground", "Sydney", "NSW"), 
	Telstra_Dome(8, 144.9474, -37.8165, "Docklands Stadium", "Melbourne", "VIC"),
	Telstra_Stadium(2, 151.0634, -33.8471, "Stadium Australia", "Sydney Olympic Park", "NSW"),
	TIO_Stadium(11, 130.8872, -12.3992, "Marrara Stadium", "Darwin", "NT"), 
	W___A___C___A___(18, 115.8797, -31.9600, "WACA Ground", "Perth", "WA"),
	Westpac_Stadium(19, 174.7858, -41.2731, "Wellington Regional Stadium", "Wellington", "NZ"),
	York_Park(3, 147.1389, -41.4258, "York Park", "Launceston", "TAS"), 
	Visy_Park(13, 144.9617, -37.7839, "Princes Park", "Carlton", "VIC"),
	Olympic_Park_Oval(20, 144.9811, -37.8247, "Olympic Park", "Melbourne", "VIC"),
	Melbourne_Airport_Training_Facility(21, 144.8406, -37.6875, "Melbourne Airport", "Melbourne", "VIC"),
	Fremantle_Oval(22, 115.7514, 32.0561, "Fremantle Oval", "Fremantle", "WA"),
	Tom_Wills_Oval(23, 151.0726, -33.8536, "Tom Wills Oval", "Sydney Olympic Park", "NSW"),
	Waverley_Park(24, 145.1886, -37.9256, "Waverley Park", "Mulgrave", "VIC"),
	Gosch__s_Paddock(25, 144.9852, -37.8180, "Yarra Park", "East Melbourne", "VIC"), // YP is a precinct, in.c MCG
	Arden_Street_Oval(26, 144.9411, 37.7992, "Arden Street Oval", "North Melbourne", "VIC"),
	Alberton_Oval(27, 138.5194, -34.8644, "Alberton Oval", "Alberton", "SA"),
	Punt_Road_Oval(28, 144.9878, -37.8222, "Punt Road Oval", "Richmond", "VIC"),
	Linen_House_Centre(29, 145.1575, -38.1052, "Linen House Centre", "Seaford", "VIC"),
	Whitten_Oval(30, 144.8867, -37.7992, "Whitten_Oval", "Footscray", "VIC"),
	;

	private final int identifier;
	private final double latitude;
	private final double longitude;
	private final String ground;
	private final String suburb;
	private final String state;

	private Location(int identifier, double longitude, double latitude, String ground, String suburb, String state) {
		this.identifier = identifier;
		this.latitude = latitude;
		this.longitude = longitude;
		this.ground = ground;
		this.suburb = suburb;
		this.state = state;
	}

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

	@Override
	public String toString() {
		return toExternal();
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getGround() {
		return ground;
	}

	public String getSuburb() {
		return suburb;
	}

	public String getState() {
		return state;
	}

	public int getIdentifier() {
		return identifier;
	}

}
