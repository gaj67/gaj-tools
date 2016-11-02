package gaj.analysis.data.vector.impl;

import java.util.Iterator;
import gaj.analysis.data.vector.IndexVector;
import gaj.common.annotations.PackagePrivate;

@PackagePrivate class DenseIndexVector implements IndexVector {

    private final int[] values;

    @PackagePrivate DenseIndexVector(int[] data) {
        this.values = data;
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public int get(int pos) {
        return values[pos];
    }

    @Override
    public Iterator<Integer> iterator() {
        return new VectorIterative<Integer>(values.length) {
            @Override
            protected Integer get(int pos) {
                return values[pos];
            }
        };
    }

}
