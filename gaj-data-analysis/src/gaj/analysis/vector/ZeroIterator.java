package gaj.analysis.vector;

import java.util.NoSuchElementException;

/*package-private*/ class ZeroIterator extends DataIterator {
	private final int length;
	private int pos = 0;

	/*package-private*/ ZeroIterator(int length) {
		this.length = length;
	}

	@Override
	public boolean hasNext() {
		return (pos < length);
	}

	@Override
	public Double next() {
		if (++pos > length) throw new NoSuchElementException("End of iteration");
		return 0.0;
	}

}
