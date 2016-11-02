package gaj.analysis.data.matrix.impl;

import gaj.analysis.data.matrix.DataMatrix;
import gaj.analysis.data.matrix.RowArrayMatrix;
import gaj.analysis.data.matrix.WritableMatrix;
import gaj.analysis.data.vector.ArrayVector;
import gaj.analysis.data.vector.DataVector;
import gaj.analysis.data.vector.WritableVector;
import gaj.analysis.data.vector.impl.VectorFactory;
import gaj.common.annotations.PackagePrivate;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@PackagePrivate class WritableRowMatrix extends DenseMatrix<WritableVector> implements WritableMatrix, RowArrayMatrix {

    private final double[][] data;

    @PackagePrivate WritableRowMatrix(double[][] data) {
        super(data.length, data[0].length);
        this.data = data;
    }

    @PackagePrivate WritableRowMatrix(int numRows, int numColumns) {
        super(numRows, numColumns);
        this.data = new double[numRows][numColumns];
    }

    @Override
    public WritableVector getRow(int row) {
        return VectorFactory.newVector(data[row]);
    }

    @Override
    public WritableVector getColumn(int column) {
        return new WritableColumnVector(data, column);
    }

    @Override
    public double norm() {
        throw new NotImplementedException();
    }

    @Override
    public double get(int row, int column) {
        return data[row][column];
    }

    @Override
    public void set(int row, int column, double value) {
        data[row][column] = value;
    }

    @Override
    public void add(double value) {
        for (double[] rowVec : data) {
            for (int column = 0; column < numColumns; column++) {
                rowVec[column] += value;
            }
        }
    }

    @Override
    public void add(int row, int column, double value) {
        data[row][column] += value;
    }

    @Override
    public void multiply(int row, int column, double value) {
        data[row][column] *= value;
    }

    @Override
    public void addRow(int row, DataVector vector) {
        final double[] theRow = data[row];
        if (vector instanceof ArrayVector) {
            final double[] values = ((ArrayVector) vector).getArray();
            for (int column = 0; column < numColumns; column++) {
                theRow[column] += values[column];
            }
        } else {
            int column = 0;
            for (double value : vector) {
                theRow[column++] += value;
            }
        }
    }

    @Override
    public void addColumn(final int column, DataVector vector) {
        if (vector instanceof ArrayVector) {
            final double[] values = ((ArrayVector) vector).getArray();
            for (int row = 0; row < numRows; row++) {
                data[row][column] += values[row];
            }
        } else {
            int row = 0;
            for (double value : vector) {
                data[row++][column] += value;
            }
        }
    }

    @Override
    public void add(DataMatrix matrix) {
        for (int row = 0; row < numRows; row++) {
            addRow(row, matrix.getRow(row));
        }
    }

    @Override
    protected void addTo(WritableMatrix matrix) {
        for (int row = 0; row < numRows; row++) {
            matrix.addRow(row, getRow(row));
        }
    }

    @Override
    public void set(DataMatrix matrix) {
        for (int row = 0; row < numRows; row++) {
            setRow(row, matrix.getRow(row));
        }
    }

    @Override
    public void setRow(final int row, DataVector vector) {
        final double[] theRow = data[row];
        if (vector instanceof ArrayVector) {
            double[] values = ((ArrayVector) vector).getArray();
            System.arraycopy(values, 0, theRow, 0, numColumns);
        } else {
            int column = 0;
            for (double value : vector) {
                theRow[column++] = value;
            }
        }
    }

    @Override
    public void setColumn(int column, DataVector vector) {
        if (vector instanceof ArrayVector) {
            final double[] values = ((ArrayVector) vector).getArray();
            for (int row = 0; row < numRows; row++) {
                data[row][column] = values[row];
            }
        } else {
            int row = 0;
            for (double value : vector) {
                data[row++][column] = value;
            }
        }
    }

    @Override
    public void multiplyRow(int row, DataVector vector) {
        final double[] theRow = data[row];
        if (vector instanceof ArrayVector) {
            final double[] values = ((ArrayVector) vector).getArray();
            for (int column = 0; column < numColumns; column++) {
                theRow[column] *= values[column];
            }
        } else {
            int column = 0;
            for (double value : vector) {
                theRow[column++] *= value;
            }
        }
    }

    @Override
    public void multiplyRow(int row, double value) {
        final double[] theRow = data[row];
        for (int column = 0; column < numColumns; column++) {
            theRow[column] *= value;
        }
    }

    @Override
    public void multiplyColumn(int column, DataVector vector) {
        if (vector instanceof ArrayVector) {
            final double[] values = ((ArrayVector) vector).getArray();
            for (int row = 0; row < numRows; row++) {
                data[row][column] *= values[row];
            }
        } else {
            int row = 0;
            for (double value : vector) {
                data[row++][column] *= value;
            }
        }
    }

    @Override
    public void multiplyColumn(int column, double value) {
        for (int row = 0; row < numRows; row++) {
            data[row][column] *= value;
        }
    }

    @Override
    public void multiply(DataMatrix matrix) {
        for (int row = 0; row < numRows; row++) {
            multiplyRow(row, matrix.getRow(row));
        }
    }

    @Override
    public double[][] getArray() {
        return data;
    }

    @Override
    public void multiply(double value) {
        for (int row = 0; row < numRows; row++) {
            final double[] theRow = data[row];
            for (int column = 0; column < numColumns; column++) {
                theRow[column] *= value;
            }
        }
    }

}
