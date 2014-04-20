package gaj.analysis.matrix;

import gaj.analysis.vector.VectorFactory;
import gaj.data.matrix.CompoundMatrix;
import gaj.data.matrix.DataMatrix;
import gaj.data.matrix.WritableMatrix;
import gaj.data.vector.DataVector;

/*package-private*/ class ScaledCompoundMatrix implements CompoundMatrix {

	private final DataMatrix matrix;
	private final double multiplier;

	/*package-private*/ ScaledCompoundMatrix(DataMatrix matrix, double multiplier) {
		this.matrix = matrix;
		this.multiplier = multiplier;
	}

	@Override
	public int numRows() {
		return matrix.numRows();
	}

	@Override
	public int numColumns() {
		return matrix.numColumns();
	}

	@Override
	public DataVector getRow(int row) {
		return VectorFactory.scale(matrix.getRow(row), multiplier, false);
	}

	@Override
	public DataVector getColumn(int col) {
		return VectorFactory.scale(matrix.getColumn(col), multiplier, false);
	}

	@Override
	public double get(int row, int col) {
		return multiplier * matrix.get(row, col);
	}

	@Override
	public double norm() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addTo(WritableMatrix matrix) {
		for (int row = 0; row < matrix.numRows(); row++) {
			DataVector rowVec = VectorFactory.scale(this.matrix.getRow(row), multiplier, false);
			rowVec.addTo(matrix.getRow(row));
		}
		
	}

}
