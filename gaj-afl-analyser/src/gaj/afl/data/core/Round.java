package gaj.afl.data.core;

/**
 * Specifies the round of the year.
 */
public enum Round {

    Round1(), Round2(), Round3(), Round4(),
    Round5(), Round6(), Round7(), Round8(),
    Round9(), Round10(), Round11(), Round12(),
    Round13(), Round14(), Round15(), Round16(),
    Round17(), Round18(), Round19(), Round20(),
    Round21(), Round22(), Round23(), Round24(),
    QualifyingFinal("QF"),
    EliminationFinal("EF"),
    SemiFinal("SF"),
    PreliminaryFinal("PF"),
    GrandFinal("GF");

    private final String externalValue;

    private Round() {
        this.externalValue = "R" + (ordinal() + 1);
    }

    private Round(String value) {
        this.externalValue = value;
    }

    public String toExternal() {
        return externalValue;
    }

    /**
     * Obtains the previous round.
     *
     * @return The previous round, or a value of null if there is no previous round.
     */
    public/* @Nullable */Round prevRound() {
        int prev = ordinal() - 1;
        return (prev < 0) ? null : values()[prev];
    }

    /**
     * Obtains the next round.
     *
     * @return The next round, or a value of null if there is no next round.
     */
    public/* @Nullable */Round nextRound() {
        int next = ordinal() + 1;
        return (next >= values().length) ? null : values()[next];
    }

    /**
     * Obtains the type from an external string representation.
     *
     * @param value - The string representation.
     * @return The match outcome.
     */
    public static Round fromExternal(String value) {
        for (Round round : values()) {
            if (round.externalValue.equals(value)) {
                return round;
            }
        }
        throw new IllegalArgumentException("Unknown match round: " + value);
    }
};
