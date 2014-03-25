package afl.features;

public class IndirectFeatureMatrix implements FeatureMatrix {

	private FeatureVector[] rows;
	private final int numRows;
	private final int numCols;

	public IndirectFeatureMatrix(FeatureVector... rowVectors) {
		this.rows = rowVectors;
		numCols = rows.length;
		numRows = rows[0].length();
	}
	
	public double[] dot(FeatureVector vector) {
		double[] mult = new double[rows.length];
		for (int i = 0; i < rows.length; i++)
			mult[i] = rows[i].dot(vector);
		return mult;
	}

	public FeatureVector get(int row) {
		return rows[row];
	}

	public double get(int row, int col) {
		return rows[row].get(col);
	}

	public int numColumns() {
		return numCols;
	}

	public int numRows() {
		return numRows;
	}

}
