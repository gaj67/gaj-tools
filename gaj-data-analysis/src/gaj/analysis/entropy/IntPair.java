package gaj.analysis.entropy;

/**
 * Encapsulates a pair of discrete observations.
 */
public class IntPair implements Comparable<IntPair> {

	private final int x;
	private final int y;
	
	public IntPair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int compareTo(IntPair pair) {
		int diff = x - pair.x;
		if (diff != 0) return diff;
		return y - pair.y;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IntPair)) return false;
		IntPair pair = (IntPair) obj;
		if (x != pair.x) return false;
		return y == pair.y;
	}

	@Override
	public int hashCode() {
		return ~x ^ y;
	}
}
