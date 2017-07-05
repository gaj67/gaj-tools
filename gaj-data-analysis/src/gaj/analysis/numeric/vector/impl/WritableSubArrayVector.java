package gaj.analysis.numeric.vector.impl;

import java.util.function.Function;
import gaj.analysis.numeric.vector.AddableVector;
import gaj.analysis.numeric.vector.ArrayVector;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.SubtractableVector;
import gaj.analysis.numeric.vector.WritableVector;
import gaj.common.annotations.PackagePrivate;

/**
 * Provides a view onto part of another vector.
 */
@PackagePrivate class WritableSubArrayVector extends DenseVectorImpl implements WritableVector {

    private final double[] values;
    protected final int start;
    protected final int end;

    @PackagePrivate WritableSubArrayVector(double[] vector, int start, int length) {
        super(length);
        this.values = vector;
        this.start = start;
        this.end = start + length;
    }

    @Override
    public double norm() {
        return _norm();
    }

    @Override
    protected double _norm() {
        double sum = 0;
        for (int pos = start; pos < end; pos++) {
            double value = values[pos];
            sum += value * value;
        }
        return Math.sqrt(sum);
    }

    @Override
    public double sum() {
        double sum = 0;
        for (int pos = start; pos < end; pos++) {
            sum += values[pos];
        }
        return sum;
    }

    @Override
    public double get(int pos) {
        return values[start + pos];
    }

    @Override
    public double dot(DataVector vector) {
        double sum = 0;
        for (int i = 0, pos = start; pos < end; pos++, i++) {
            sum += vector.get(i) * values[pos];
        }
        return sum;
    }

    @Override
    public void addTo(AddableVector vector) {
        for (int i = 0, pos = start; pos < end; pos++, i++) {
            vector.add(i, values[pos]);
        }
    }

    @Override
    public void subtractFrom(SubtractableVector vector) {
        int i = 0;
        for (int pos = start; pos < end; pos++) {
            vector.subtract(i++, values[pos]);
        }
    }

    @Override
    public void set(double value) {
        for (int pos = start; pos < end; pos++) {
            values[pos] = value;
        }
    }

    @Override
    public void set(int pos, double value) {
        values[start + pos] = value;
    }

    @Override
    public void set(DataVector vector) {
        if (vector instanceof ArrayVector) {
            double[] data = ((ArrayVector) vector).getArray();
            System.arraycopy(data, 0, values, start, length);
        } else {
            for (int i = 0, pos = start; pos < end; pos++, i++) {
                values[pos] = vector.get(i);
            }
        }
    }

    @Override
    public void add(double value) {
        for (int i = start; i < end; i++) {
            values[i] += value;
        }
    }

    @Override
    public void subtract(double value) {
        for (int i = start; i < end; i++) {
            values[i] -= value;
        }
    }

    @Override
    public void add(int pos, double value) {
        values[start + pos] += value;
    }

    @Override
    public void subtract(int pos, double value) {
        values[start + pos] -= value;
    }

    @Override
    public void add(DataVector vector) {
        if (vector instanceof ArrayVector) {
            double[] data = ((ArrayVector) vector).getArray();
            for (int i = 0, pos = start; i < length; i++, pos++) {
                values[pos] += data[i];
            }
        } else {
            for (int i = 0, pos = start; pos < end; pos++, i++) {
                values[pos] += vector.get(i);
            }
        }
    }

    @Override
    public void subtract(DataVector vector) {
        if (vector instanceof ArrayVector) {
            double[] data = ((ArrayVector) vector).getArray();
            for (int i = 0, pos = start; i < length; i++, pos++) {
                values[pos] -= data[i];
            }
        } else {
            for (int i = 0, pos = start; pos < end; pos++, i++) {
                values[pos] -= vector.get(i);
            }
        }
    }

    @Override
    public void multiply(int pos, double value) {
        values[start + pos] *= value;
    }

    @Override
    public void multiply(double value) {
        for (int pos = start; pos < end; pos++) {
            values[pos] *= value;
        }
    }

    @Override
    public void multiply(DataVector vector) {
        if (vector instanceof ArrayVector) {
            double[] data = ((ArrayVector) vector).getArray();
            for (int i = 0, pos = start; i < length; i++, pos++) {
                values[pos] *= data[i];
            }
        } else {
            for (int i = 0, pos = start; pos < end; pos++, i++) {
                values[pos] *= vector.get(i);
            }
        }
    }

    @Override
    public void apply(Function<Double, Double> func) {
        for (int pos = start; pos < end; pos++) {
            values[pos] = func.apply(values[pos]);
        }
    }

}
