package gaj.afl.data.core;

/**
 * Specifies the location (ground) of a match.
 */
public enum Location {

    AAMI_Stadium(0, -34.8798, 138.4936, "Football Park", "West Lakes", "SA"),
    Football_Park(0, -34.8798, 138.4936, "Football Park", "West Lakes", "SA"),

    Adelaide_Oval(1, -34.9156, 138.5961, "Adelaide Oval", "Adelaide", "SA"),

    ANZ_Stadium(2, -33.8471, 151.0634, "Stadium Australia", "Sydney Olympic Park", "NSW"),
    Stadium_Australia(2, -33.8471, 151.0634, "Stadium Australia", "Sydney Olympic Park", "NSW"),
    Telstra_Stadium(2, -33.8471, 151.0634, "Stadium Australia", "Sydney Olympic Park", "NSW"),

    Aurora_Stadium(3, -41.4258, 147.1389, "York Park", "Launceston", "TAS"),
    York_Park(3, -41.4258, 147.1389, "York Park", "Launceston", "TAS"),

    Blacktown_ISP(4, -33.7694, 150.8592, "International Sportspark", "Rooty Hill", "NSW"),

    Blundstone_Arena(5, -42.8773, 147.3735, "Bellerive Oval", "Hobart", "TAS"),

    Carrara(6, -28.0063, 153.3669, "Carrara Stadium", "Gold Coast", "QLD"),
    Gold_Coast_Stadium(6, -28.0063, 153.3669, "Carrara Stadium", "Gold Coast", "QLD"),
    Metricon_Stadium(6, -28.0063, 153.3669, "Carrara Stadium", "Gold Coast", "QLD"),

    Cazaly__s_Stadium(7, -16.9358, 145.7492, "Cazaly's Stadium", "Westcourt", "QLD"),

    Colonial_Stadium(8, -37.8165, 144.9474, "Docklands Stadium", "Melbourne", "VIC"),
    Etihad_Stadium(8, -37.8165, 144.9474, "Docklands Stadium", "Melbourne", "VIC"),
    Telstra_Dome(8, -37.8165, 144.9474, "Docklands Stadium", "Melbourne", "VIC"),

    Gabba(9, -27.4859, 153.0381, "Brisbane Cricket Ground", "Brisbane", "QLD"),

    Manuka_Oval(10, -35.318102,149.134598, "Manuka Oval", "Griffith", "ACT"),
    StarTrack_Oval(10, -35.318102,149.134598, "Manuka Oval", "Griffith", "ACT"),

    Marrara(11, -12.3992, 130.8872, "Marrara Stadium", "Darwin", "NT"),
    TIO_Stadium(11, -12.3992, 130.8872, "Marrara Stadium", "Darwin", "NT"),

    Melbourne_Cricket_Ground(12, -37.8200, 144.9832, "Melbourne Cricket Ground", "Melbourne", "VIC"),

    Optus_Oval(13, -37.7839, 144.9617, "Princes Park", "Carlton", "VIC"),
    Princes_Park(13, -37.7839, 144.9617, "Princes Park", "Carlton", "VIC"),
    Visy_Park(13, -37.7839, 144.9617, "Princes Park", "Carlton", "VIC"),

    Patersons_Stadium(14, -31.9444, 115.83, "Subiaco Oval", "Subiaco", "WA"),
    Subiaco_Oval(14, -31.9444, 115.83, "Subiaco Oval", "Subiaco", "WA"),

    Shell_Stadium(15, -38.1581, 144.3546, "Kardinia Park", "South Geelong", "VIC"),
    Simonds_Stadium(15, -38.1581, 144.3546, "Kardinia Park", "South Geelong", "VIC"),
    Skilled_Stadium(15, -38.1581, 144.3546, "Kardinia Park", "South Geelong", "VIC"),
    Kardinia_Park(15, -38.1581, 144.3546, "Kardinia Park", "South Geelong", "VIC"),

    Skoda_Stadium(16, -33.8431, 151.0678, "Sydney Showground Stadium", "Sydney Olympic Park", "NSW"),
    Spotless_Stadium(16, -33.8431, 151.0678, "Sydney Showground Stadium", "Sydney Olympic Park", "NSW"),

    Sydney_Cricket_Ground(17, -33.8917, 151.2247, "Sydney Cricket Ground", "Sydney", "NSW"),

    W___A___C___A___(18, -31.9600, 115.8797, "WACA Ground", "Perth", "WA"),

    Westpac_Stadium(19, -41.2731, 174.7858, "Wellington Regional Stadium", "Wellington", "NZ"),

    Olympic_Park_Oval(20, -37.8247, 144.9811, "Olympic Park", "Melbourne", "VIC"),

    Melbourne_Airport_Training_Facility(21, -37.6875, 144.8406, "Melbourne Airport", "Melbourne", "VIC"),

    Fremantle_Oval(22, 32.0561, 115.7514, "Fremantle Oval", "Fremantle", "WA"),

    Tom_Wills_Oval(23, -33.8536, 151.0726, "Tom Wills Oval", "Sydney Olympic Park", "NSW"),

    Waverley_Park(24, -37.9256, 145.1886, "Waverley Park", "Mulgrave", "VIC"),

    Gosch__s_Paddock(25, -37.8180, 144.9852, "Yarra Park", "East Melbourne", "VIC"), // YP is a precinct, in MCG

    Arden_Street_Oval(26, 37.7992, 144.9411, "Arden Street Oval", "North Melbourne", "VIC"),

    Alberton_Oval(27, -34.8644, 138.5194, "Alberton Oval", "Alberton", "SA"),

    Punt_Road_Oval(28, -37.8222, 144.9878, "Punt Road Oval", "Richmond", "VIC"),

    Linen_House_Centre(29, -38.1052, 145.1575, "Linen House Centre", "Seaford", "VIC"),

    Whitten_Oval(30, -37.7992, 144.8867, "Whitten_Oval", "Footscray", "VIC"),

    Moorabbin_Oval(31, -37.9375, 145.0439, "Moorabbin Oval", "Moorabbin", "VIC"), // St Kilda old ground.
    Linen_House_Oval(31, -37.9375, 145.0439, "Moorabbin Oval", "Moorabbin", "VIC"),

    TIO_Traeger_Park(32, -23.709039, 133.875083, "Traeger Park", "Alice Springs", "NT"),

    Windy_Hill(33, -37.751919, 144.919538, "Essendon Recreation Reserve", "Essendon", "VIC"),

    Victoria_Park(34, -37.798255, 144.996443, "Victoria Park", "Abbotsford", "VIC"),

    North_Hobart(35, -42.867778, 147.315833, "North Hobart Oval", "North Hobart", "TAS"),

    Western_Oval(-1, 0, 0, "Western Oval", "Geelong North", "VIC"),

    AFL_Park(-1, 0, 0, "?", "?", "?"),
    ;

    private final int identifier;
    private final double latitude;
    private final double longitude;
    private final String ground;
    private final String suburb;
    private final String state;

    private Location(int identifier, double latitude, double longitude, String ground, String suburb, String state) {
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
