package gaj.impl.matrix;

import gaj.data.vector.DataVector;

public abstract class DenseMatrix<T extends DataVector> extends AbstractMatrix<T> {

	protected DenseMatrix(int numRows, int numColumns) {
		super(numRows, numColumns);
	}

	@Override
	public boolean isDense() {
		return true;
	}

	@Override
	public boolean isSparse() {
		return false;
	}

	@Override
	public boolean isCompound() {
		return false;
	}

}
