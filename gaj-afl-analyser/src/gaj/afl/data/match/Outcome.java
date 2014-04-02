package gaj.afl.data.match;

/**
 * Specifies the outcome of a match, relative to the ordering
 * of the two teams.
 */
public enum Outcome {
	Win("defeated"), Loss("lost to"), Draw("drew with");

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
	 * Indicates the match status from the other team's point of view.
	 * 
	 * @return
	 */
	public Outcome reverse() {
		switch (this) {
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
};
