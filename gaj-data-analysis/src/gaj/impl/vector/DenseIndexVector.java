package gaj.impl.vector;

import java.util.Iterator;

import gaj.data.vector.IndexVector;

/*package-private*/ class DenseIndexVector implements IndexVector {

	private final int[] values;

	/*package-private*/ DenseIndexVector(int[] data) {
		this.values = data;
	}

	@Override
	public int size() {
		return values.length;
	}

	@Override
	public int get(int pos) {
		return values[pos];
	}

	@Override
	public Iterator<Integer> iterator() {
		return new VectorIterative<Integer>(values.length) {
			@Override
			protected Integer get(int pos) {
				return values[pos];
			}
		};
	}

}
