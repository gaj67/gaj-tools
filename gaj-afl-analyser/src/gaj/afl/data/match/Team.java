package gaj.afl.data.match;

/**
 * Enumerates the various AFL teams.
 */
public enum Team {
	
	Adelaide, Brisbane_Lions, Carlton, Collingwood, Essendon, 
	Fremantle, Geelong, Hawthorn, Melbourne, North_Melbourne, 
	Port_Adelaide, Richmond, St_Kilda, Sydney, West_Coast, 
	Western_Bulldogs, Gold_Coast, GWS_Giants;

	/**
	 * Obtains the type from an external string representation.
	 * 
	 * @param value - The string representation.
	 * @return The team type.
	 */
	public static Team fromExternal(String value) {
		Team team = valueOf(value.replace(' ', '_'));
		if (team == null)
			throw new IllegalArgumentException("Unknown team: " + value);
		return team;
	}
	
	/**
	 * Obtains an external string representation of the type.
	 * 
	 * @return The string representation.
	 */
	public String toExternal() {
		return name().replace('_', ' ');
	}
};
