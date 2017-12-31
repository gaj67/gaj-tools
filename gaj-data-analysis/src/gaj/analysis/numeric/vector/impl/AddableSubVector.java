package gaj.analysis.numeric.vector.impl;

import gaj.analysis.numeric.vector.AddableVector;
import gaj.analysis.numeric.vector.DataVector;

/**
 * Implements a vector as a reference to a contiguous subsequence of elements in
 * another vector.
 */
public class AddableSubVector extends SubVector implements AddableVector {

    private final AddableVector vector;

    public AddableSubVector(AddableVector vector, int start, int length) {
        super(vector, start, length);
        this.vector = vector;
    }

    @Override
    public double norm() {
        return _norm();
    }

    @Override
    public void add(double value) {
        for (int pos = start; pos < end; pos++) {
            vector.add(pos, value);
        }
    }

    @Override
    public void add(int i, double value) {
        vector.add(start + i, value);
    }

    @Override
    public void add(DataVector vector) {
        for (int i = 0, pos = start; pos < end; pos++, i++) {
            this.vector.add(pos, vector.get(i));
        }
    }

}
