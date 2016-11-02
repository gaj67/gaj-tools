package gaj.analysis.data.vector.impl;

import gaj.analysis.data.vector.AddableVector;
import gaj.analysis.data.vector.DataVector;

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
        for (int i = start; i < end; i++) {
            vector.add(i, value);
        }
    }

    @Override
    public void add(int pos, double value) {
        vector.add(start + pos, value);
    }

    @Override
    public void add(DataVector vector) {
        int pos = start;
        for (double value : vector) {
            this.vector.add(pos++, value);
        }
    }

}
