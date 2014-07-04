package gaj.data.markov;

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

	/**
	 * Indicates that the sequence has a known start.
	 * 
	 * @return A value of true (or false) if the sequence has (or does not have) an implicit initial state.
	 */
	boolean isInitiated() {
		return (this == Start || this == Full);
	}
	
	/**
	 * Indicates that the sequence has a known end.
	 * 
	 * @return A value of true (or false) if the sequence has (or does not have) an implicit terminal state.
	 */
	boolean isTerminated() {
		return (this == End || this == Full);
	}	

}