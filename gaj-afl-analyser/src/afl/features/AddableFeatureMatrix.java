package afl.features;

import java.util.ArrayList;
import java.util.List;

public class AddableFeatureMatrix implements FeatureMatrix {

	final private List<FeatureVector> rows = new ArrayList<FeatureVector>();
	private int numRows = 0;
	private int numCols = -1;

	public AddableFeatureMatrix(List<FeatureVector> rowVectors) {
		add(rowVectors);
	}
	
	public AddableFeatureMatrix() {}

	public void add(FeatureVector features) {
		rows.add(features);
		if (numCols < 0)
			numCols = features.length();
		numRows++;
	}

	public void add(List<FeatureVector> rowVectors) {
		rows.addAll(rowVectors);
		if (numCols < 0)
			numCols = rowVectors.get(0).length();
		numRows += rowVectors.size();
	}
	
	public double[] dot(FeatureVector vector) {
		final int size = rows.size();
		final double[] mult = new double[size];
		for (int i = 0; i < size; i++)
			mult[i] = rows.get(i).dot(vector);
		return mult;
	}

	public FeatureVector get(int row) {
		return rows.get(row);
	}

	public double get(int row, int col) {
		return rows.get(row).get(col);
	}

	public int numColumns() {
		return numCols;
	}

	public int numRows() {
		return numRows;
	}

}
