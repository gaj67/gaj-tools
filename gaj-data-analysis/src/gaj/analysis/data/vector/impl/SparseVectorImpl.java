package gaj.analysis.data.vector.impl;

import java.util.Iterator;
import gaj.analysis.data.vector.AddableVector;
import gaj.analysis.data.vector.DataVector;

/**
 * Implements a data vector as an array of index/value pairs, with ascending indices.
 */
/* package-private */class SparseVectorImpl extends SparseVector {

    /* package-private */final int[] indices;
    /* package-private */final double[] values;

    /* package-private */SparseVectorImpl(int length, int[] indices, double[] values) {
        super(length);
        this.indices = indices;
        this.values = values;
    }

    /* package-private */SparseVectorImpl(int length) {
        super(length);
        this.indices = new int[0];
        this.values = new double[0];
    }

    @Override
    public double get(int pos) {
        if (pos < 0 && pos >= length)
            throw new IndexOutOfBoundsException("Bad index: " + pos);
        for (int i = 0; i < indices.length; i++) {
            if (pos < indices[i])
                return 0;
            if (pos == indices[i])
                return values[i];
        }
        return 0;
    }

    @Override
    public Iterator<Double> iterator() {
        return new VectorIterative<Double>(length) {
            /** Local position in the index table. */
            private int index = 0;

            @Override
            protected Double get(int pos) {
                while (index < indices.length && pos > indices[index])
                    index++;
                if (index >= indices.length)
                    return 0.0;
                if (pos < indices[index])
                    return 0.0;
                return values[index++];
            }
        };
    }

    @Override
    public double dot(DataVector vector) {
        double sum = 0;
        for (int i = 0; i < indices.length; i++)
            sum += values[i] * vector.get(indices[i]);
        return sum;
    }

    @Override
    protected double _norm() {
        double sum = 0;
        for (double value : values)
            sum += value * value;
        return Math.sqrt(sum);
    }

    @Override
    public double sum() {
        double sum = 0;
        for (double value : values)
            sum += value;
        return sum;
    }

    @Override
    public void addTo(AddableVector vector) {
        for (int i = 0; i < values.length; i++)
            vector.add(indices[i], values[i]);
    }

}
