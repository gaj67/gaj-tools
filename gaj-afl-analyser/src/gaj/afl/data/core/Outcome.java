package gaj.afl.data.core;

/**
 * Specifies the outcome of a match, relative to the home team.
 */
public enum Outcome {
    Win("defeated"), Loss("lost to"), Draw("drew with"), Bye("did not play");

    private final String externalValue;

    private Outcome(String value) {
        this.externalValue = value;
    }

    public String toExternal() {
        return externalValue;
    }

    /**
     * Obtains the type from an external string representation.
     * 
     * @param value - The string representation.
     * @return The match outcome.
     */
    public static Outcome fromExternal(String value) {
        for (Outcome outcome : values()) {
            if (outcome.externalValue.equals(value))
                return outcome;
        }
        throw new IllegalArgumentException("Unknown match outcome: " + value);
    }

    /**
     * Indicates the match status from the away team's point of view.
     * 
     * @return The reverse of the home team's outcome.
     */
    public Outcome reverse() {
        switch (this) {
            case Bye:
                // There is no home or away team for a bye.
                //throw new IllegalStateException("Cannot reverse match outcome " + this);
                return Bye;
            case Draw:
                return Draw;
            case Loss:
                return Win;
            case Win:
                return Loss;
            default:
                throw new IllegalStateException("Unknown match outcome " + this);
        }
    }

}