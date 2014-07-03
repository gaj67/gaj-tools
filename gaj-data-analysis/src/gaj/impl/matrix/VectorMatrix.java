package gaj.impl.matrix;

import gaj.data.matrix.DataMatrix;
import gaj.impl.vector.AbstractVector;

/**
 * Presents a matrix as a flat vector.
 */
public class VectorMatrix extends AbstractVector {

	protected final DataMatrix matrix;
	protected final int numColumns;

	protected VectorMatrix(DataMatrix matrix) {
		super(matrix.numRows() * matrix.numColumns());
		this.matrix = matrix;
		this.numColumns = matrix.numColumns();
	}

	@Override
	public double get(int pos) {
		// Row-wise, pos = numColumns * row + column.
		return matrix.get(pos / numColumns, pos % numColumns);
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
