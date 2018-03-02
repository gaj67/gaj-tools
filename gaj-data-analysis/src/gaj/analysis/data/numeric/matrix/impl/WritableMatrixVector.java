package gaj.analysis.data.numeric.matrix.impl;

import java.util.function.Function;
import gaj.analysis.data.numeric.matrix.WritableMatrix;
import gaj.analysis.data.numeric.vector.DataVector;
import gaj.analysis.data.numeric.vector.WritableVector;

/**
 * Presents a non-flat writable matrix as a flat writable vector.
 */
public class WritableMatrixVector extends RowMatrixVector implements WritableVector {

    protected final int numRows;

    protected WritableMatrixVector(WritableMatrix matrix) {
        super(matrix);
        this.numRows = matrix.numRows();
    }

    @Override
    public void set(int pos, double value) {
        // Row-wise, pos = numColumns * row + column.
        ((WritableMatrix) matrix).set(pos / numColumns, pos % numColumns, value);
    }

    @Override
    public void set(DataVector vector) {
        final WritableMatrix _matrix = ((WritableMatrix) matrix);
        int pos = 0;
        for (int row = 0; row < numRows; row++) {
            WritableVector rowVec = _matrix.getRow(row);
            for (int column = 0; column < numColumns; column++) {
                rowVec.set(column, vector.get(pos++));
            }
        }
    }

    @Override
    public void add(double value) {
        // Row-wise, pos = numColumns * row + column.
        for (int pos = 0; pos < length; pos++) {
            ((WritableMatrix) matrix).add(pos / numColumns, pos % numColumns, value);
        }
    }

    @Override
    public void subtract(double value) {
        // Row-wise, pos = numColumns * row + column.
        for (int pos = 0; pos < length; pos++) {
            ((WritableMatrix) matrix).subtract(pos / numColumns, pos % numColumns, value);
        }
    }

    @Override
    public void add(int pos, double value) {
        // Row-wise, pos = numColumns * row + column.
        ((WritableMatrix) matrix).add(pos / numColumns, pos % numColumns, value);
    }

    @Override
    public void subtract(int pos, double value) {
        // Row-wise, pos = numColumns * row + column.
        ((WritableMatrix) matrix).subtract(pos / numColumns, pos % numColumns, value);
    }

    @Override
    public void add(DataVector vector) {
        final WritableMatrix _matrix = ((WritableMatrix) matrix);
        int pos = 0;
        for (int row = 0; row < numRows; row++) {
            WritableVector rowVec = _matrix.getRow(row);
            for (int column = 0; column < numColumns; column++) {
                rowVec.add(column, vector.get(pos++));
            }
        }
    }

    @Override
    public void subtract(DataVector vector) {
        final WritableMatrix _matrix = ((WritableMatrix) matrix);
        int pos = 0;
        for (int row = 0; row < numRows; row++) {
            WritableVector rowVec = _matrix.getRow(row);
            for (int column = 0; column < numColumns; column++) {
                rowVec.subtract(column, vector.get(pos++));
            }
        }
    }

    @Override
    public void multiply(int pos, double value) {
        ((WritableMatrix) matrix).multiply(pos / numColumns, pos % numColumns, value);
    }

    @Override
    public void multiply(double value) {
        final WritableMatrix _matrix = ((WritableMatrix) matrix);
        _matrix.multiply(value);
    }

    @Override
    public void multiply(DataVector vector) {
        final WritableMatrix _matrix = ((WritableMatrix) matrix);
        int pos = 0;
        for (int row = 0; row < numRows; row++) {
            WritableVector rowVec = _matrix.getRow(row);
            for (int column = 0; column < numColumns; column++) {
                rowVec.multiply(column, vector.get(pos++));
            }
        }
    }

    @Override
    public void set(double value) {
        // ((WritableMatrix) matrix).set(value);
        final WritableMatrix _matrix = (WritableMatrix) matrix;
        for (int row = 0; row < numRows; row++) {
            WritableVector rowVec = _matrix.getRow(row);
            rowVec.set(value);
        }
    }

    @Override
    public void apply(Function<Double, Double> func) {
        // ((WritableMatrix) matrix).apply(func);
        final WritableMatrix _matrix = (WritableMatrix) matrix;
        for (int row = 0; row < numRows; row++) {
            WritableVector rowVec = _matrix.getRow(row);
            rowVec.apply(func);
        }
    }

}
