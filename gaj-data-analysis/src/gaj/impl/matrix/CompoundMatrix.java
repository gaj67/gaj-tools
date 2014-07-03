package gaj.impl.matrix;

import gaj.data.vector.DataVector;

public abstract class CompoundMatrix<T extends DataVector> extends AbstractMatrix<T> {

	protected CompoundMatrix(int numRows, int numColumns) {
		super(numRows, numColumns);
	}

	@Override
	public boolean isDense() {
		return false;
	}

	@Override
	public boolean isSparse() {
		return false;
	}

	@Override
	public boolean isCompound() {
		return true;
	}

}
