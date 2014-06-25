package gaj.analysis.vector;

import gaj.data.vector.DataVector;
import gaj.data.vector.WritableVector;

public class WritableSubVector extends SubVector implements WritableVector {

	private final WritableVector vector;

	public WritableSubVector(WritableVector vector, int start, int length) {
		super(vector, start, length);
		this.vector = vector;
	}

	@Override
	public void set(int pos, double value) {
		vector.set(start + pos, value);
	}

	@Override
	public void add(int pos, double value) {
		vector.add(start + pos, value);
	}

	@Override
	public void set(DataVector vector) {
		int pos = start;
		for (double value : vector)
			this.vector.set(pos++, value);
	}

	@Override
	public void add(DataVector vector) {
		int pos = start;
		for (double value : vector)
			this.vector.add(pos++, value);
	}

	@Override
	public void multiply(int pos, double value) {
		vector.multiply(start + pos, value);
	}

	@Override
	public void multiply(double value) {
		for (int pos = start; pos < end; pos++)
			vector.multiply(pos, value);
	}

	@Override
	public void multiply(DataVector vector) {
		for (int i = 0, pos = start; i < length; i++, pos++)
			this.vector.multiply(pos, vector.get(i));
	}

}
