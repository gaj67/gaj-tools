package gaj.analysis.matrix;

import gaj.analysis.vector.VectorFactory;
import gaj.data.matrix.DataMatrix;
import gaj.data.matrix.WritableMatrix;
import gaj.data.vector.DataVector;

/**
 * Implements the scaling of an arbitrary data matrix.
 */
/*package-private*/ class ScaledMatrix extends AbstractMatrix<DataVector> implements DataMatrix {

	private final DataMatrix matrix;
	private final double multiplier;

	/*package-private*/ ScaledMatrix(DataMatrix matrix, double multiplier) {
		super(matrix.numRows(), matrix.numColumns());
		this.matrix = matrix;
		this.multiplier = multiplier;
	}

	@Override
	public double get(int row, int column) {
		return multiplier * matrix.get(row, column);
	}

	@Override
	public DataVector getRow(int row) {
		return VectorFactory.scale(matrix.getRow(row), multiplier);
	}

	@Override
	public DataVector getColumn(int column) {
		return VectorFactory.scale(matrix.getColumn(column), multiplier);
	}

	@Override
	public double norm() {
		return multiplier * matrix.norm();
	}

	@Override
	public void addTo(WritableMatrix matrix) {
		for (int row = 0; row < numRows; row++)
			matrix.addRow(row, getRow(row));
	}

}
