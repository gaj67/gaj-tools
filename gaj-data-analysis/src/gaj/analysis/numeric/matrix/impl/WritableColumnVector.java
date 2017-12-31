package gaj.analysis.numeric.matrix.impl;

import java.util.function.Function;
import gaj.analysis.numeric.vector.ArrayVector;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.DenseVector;
import gaj.analysis.numeric.vector.WritableVector;
import gaj.analysis.numeric.vector.impl.AbstractVector;
import gaj.common.annotations.PackagePrivate;

/**
 * Wraps the specified column of a row-based, dense matrix.
 */
@PackagePrivate class WritableColumnVector extends AbstractVector implements DenseVector, WritableVector {

    private final double[][] data;
    private final int column;

    @PackagePrivate WritableColumnVector(double[][] data, int column) {
        super(data.length);
        this.data = data;
        this.column = column;
    }

    @Override
    public double get(int row) {
        return data[row][column];
    }

    @Override
    public void set(int row, double value) {
        data[row][column] = value;
    }

    @Override
    public void add(double value) {
        for (int row = 0; row < length; row++) {
            data[row][column] += value;
        }
    }

    @Override
    public void subtract(double value) {
        for (int row = 0; row < length; row++) {
            data[row][column] -= value;
        }
    }

    @Override
    public void add(int row, double value) {
        data[row][column] += value;
    }

    @Override
    public void subtract(int row, double value) {
        data[row][column] -= value;
    }

    @Override
    public void set(DataVector vector) {
        if (vector instanceof ArrayVector) {
            final double[] values = ((ArrayVector) vector).getArray();
            for (int row = 0; row < length; row++) {
                data[row][column] = values[row];
            }
        } else {
            for (int row = 0; row < length; row++) {
                data[row][column] = vector.get(row);
            }
        }
    }

    @Override
    public void add(DataVector vector) {
        if (vector instanceof ArrayVector) {
            final double[] values = ((ArrayVector) vector).getArray();
            for (int row = 0; row < length; row++) {
                data[row][column] += values[row];
            }
        } else {
            for (int row = 0; row < length; row++) {
                data[row][column] += vector.get(row);
            }
        }
    }

    @Override
    public void subtract(DataVector vector) {
        if (vector instanceof ArrayVector) {
            final double[] values = ((ArrayVector) vector).getArray();
            for (int row = 0; row < length; row++) {
                data[row][column] -= values[row];
            }
        } else {
            for (int row = 0; row < length; row++) {
                data[row][column] -= vector.get(row);
            }
        }
    }

    @Override
    public void multiply(int row, double value) {
        data[row][column] *= value;
    }

    @Override
    public void multiply(double value) {
        for (int row = 0; row < length; row++) {
            data[row][column] *= value;
        }
    }

    @Override
    public void multiply(DataVector vector) {
        if (vector instanceof ArrayVector) {
            final double[] values = ((ArrayVector) vector).getArray();
            for (int row = 0; row < length; row++) {
                data[row][column] *= values[row];
            }
        } else {
            for (int row = 0; row < length; row++) {
                data[row][column] *= vector.get(row);
            }
        }
    }

    @Override
    public void set(double value) {
        for (int row = 0; row < length; row++) {
            data[row][column] = value;
        }
    }

    @Override
    public void apply(Function<Double, Double> func) {
        for (int row = 0; row < length; row++) {
            data[row][column] = func.apply(data[row][column]);
        }
    }

}
