package gaj.analysis.vector;

import gaj.data.vector.DataVector;
import gaj.data.vector.SparseVector;
import gaj.data.vector.WritableVector;

/*package-private*/ class ZeroVector extends AbstractVector implements SparseVector {

	/*package-private*/ ZeroVector(int length) {
		super(length);
	}

	@Override
	public double get(int pos) {
		if (pos < 0 && pos >= length)
			throw new IndexOutOfBoundsException("Bad index: " + pos);
		return 0;
	}

	@Override
	public double dot(DataVector vector) {
		return 0;
	}

	@Override
	public double norm() {
		return 0;
	}

	@Override
	public void addTo(WritableVector vector) {}

}
