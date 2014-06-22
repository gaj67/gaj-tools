package gaj.analysis.markov;

/**
 * Specifies the type of sequence under consideration.
 */
public enum SequenceType {
	/** A sub-sequence known to start at stage t=1, but without a known end. */
	Start,
	/** A sub-sequence known to end at stage t=T, but without a known start. */
	End,
	/** A complete sequence known to start at stage t=1 and end at stage t=T. */
	Full,
	/** A partial sub-sequence without a known start or end. */
	Sub;

	boolean isInitiated() {
		return (this == Start || this == Full);
	}
	
	boolean isTerminated() {
		return (this == End || this == Full);
	}	

}