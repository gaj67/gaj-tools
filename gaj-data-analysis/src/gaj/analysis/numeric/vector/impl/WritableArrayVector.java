package gaj.analysis.numeric.vector.impl;

import java.util.Arrays;
import gaj.analysis.numeric.vector.AddableVector;
import gaj.analysis.numeric.vector.ArrayVector;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.WritableVector;
import gaj.common.annotations.PackagePrivate;

/**
 * Implements a data vector as a true array of numbers.
 */
@PackagePrivate class WritableArrayVector extends DenseVector implements ArrayVector, WritableVector {

    private final double[] data;

    /**
     * Initialises a zero-valued vector.
     *
     * @param length - The length of the vector.
     */
    @PackagePrivate WritableArrayVector(int length) {
        super(length);
        this.data = new double[length];
    }

    /**
     * Initialises an array-valued vector.
     *
     * @param values - The array of values.
     */
    @PackagePrivate WritableArrayVector(double[] values) {
        super(values.length);
        this.data = values;
    }

    /**
     * Initialises a copy of an array-valued vector.
     *
     * @param vector - The vector of values.
     */
    @PackagePrivate WritableArrayVector(ArrayVector vector) {
        super(vector.size());
        this.data = Arrays.copyOf(vector.getArray(), vector.size());
    }

    @Override
    public double get(int pos) {
        return data[pos];
    }

    @Override
    public double dot(DataVector vector) {
        double sum = 0;
        int i = 0;
        for (double value : vector) {
            sum += data[i++] * value;
        }
        return sum;
    }

    @Override
    public double norm() {
        return _norm();
    }

    @Override
    protected double _norm() {
        double sum = 0;
        for (double value : data) {
            sum += value * value;
        }
        return Math.sqrt(sum);
    }

    @Override
    public void addTo(AddableVector vector) {
        for (int i = 0; i < data.length; i++) {
            vector.add(i, data[i]);
        }
    }

    @Override
    public void set(int pos, double value) {
        data[pos] = value;
    }

    @Override
    public void add(double value) {
        for (int i = 0; i < length; i++) {
            data[i] += value;
        }
    }

    @Override
    public void add(int pos, double value) {
        data[pos] += value;
    }

    @Override
    public void set(DataVector vector) {
        if (vector instanceof ArrayVector) {
            double[] values = ((ArrayVector) vector).getArray();
            System.arraycopy(values, 0, data, 0, length);
        } else {
            int i = 0;
            for (double value : vector) {
                data[i++] = value;
            }
        }
    }

    @Override
    public void add(DataVector vector) {
        if (vector instanceof ArrayVector) {
            double[] values = ((ArrayVector) vector).getArray();
            for (int i = 0; i < length; i++) {
                data[i] += values[i];
            }
        } else {
            int i = 0;
            for (double value : vector) {
                data[i++] += value;
            }
        }
    }

    @Override
    public double[] getArray() {
        return data;
    }

    @Override
    public void multiply(int pos, double value) {
        data[pos] *= value;
    }

    @Override
    public void multiply(double value) {
        for (int i = 0; i < length; i++) {
            data[i] *= value;
        }
    }

    @Override
    public void multiply(DataVector vector) {
        if (vector instanceof ArrayVector) {
            double[] values = ((ArrayVector) vector).getArray();
            for (int i = 0; i < length; i++) {
                data[i] *= values[i];
            }
        } else {
            int i = 0;
            for (double value : vector) {
                data[i++] *= value;
            }
        }
    }

    @Override
    public double sum() {
        double sum = 0;
        for (double value : data) {
            sum += value;
        }
        return sum;
    }

}
