package gaj.afl.data.core;

/**
 * Enumerates the various AFL teams.
 */
public enum Team {
    Adelaide(1), Brisbane_Lions(2), Carlton(14), Collingwood(3), Essendon(5),
    Fremantle(11), Geelong(4), Hawthorn(16), Melbourne(13), North_Melbourne(6),
    Port_Adelaide(9), Richmond(8), St_Kilda(12), Sydney(15), West_Coast(10),
    Western_Bulldogs(7), Gold_Coast(19), GWS_Giants(21),

    Brisbane_Bears(2), Fitzroy(2);

    private final int identifier;

    private Team(int identifier) {
        this.identifier = identifier;
    }

    public int getIdentifier() {
        return identifier;
    }

    /**
     * Obtains the type from an external string representation.
     *
     * @param value - The string representation.
     * @return The team type.
     */
    public static Team fromExternal(String value) {
        Team team = valueOf(value.replace(' ', '_'));
        if (team == null) {
            throw new IllegalArgumentException("Unknown team: " + value);
        }
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
