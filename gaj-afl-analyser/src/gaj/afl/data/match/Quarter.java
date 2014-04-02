package gaj.afl.data.match;

/**
 * Specifies which quarter of a match is of interest.
 */
public enum Quarter {

	First, Second, Third, Fourth;

	public String toString() {
		return "Q" + (ordinal() + 1);
	}
}
