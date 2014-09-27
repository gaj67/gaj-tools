package gaj.analysis.entropy;

/**
 * Encapsulates a pair of discrete observations.
 */
public class DiscretePair<X, Y> implements Comparable<DiscretePair<X, Y>> {

	private final X x;
	private final Y y;
	
	public DiscretePair(X x, Y y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int compareTo(DiscretePair<X, Y> pair) {
		int diff = x.hashCode() - pair.x.hashCode();
		if (diff != 0) return diff;
		return y.hashCode() - pair.y.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DiscretePair<?, ?>)) return false;
		@SuppressWarnings("unchecked")
		DiscretePair<X, Y> pair = (DiscretePair<X, Y>) obj;
		if (x.hashCode() != pair.x.hashCode()) return false;
		return y.hashCode() == pair.y.hashCode();
	}

}
