package gaj.analysis.numeric.vector.impl;

import java.util.Iterator;
import gaj.analysis.numeric.vector.AddableVector;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.SubtractableVector;

/**
 * The basis for all vector implementations.
 * <p/>
 * The default method implementations assume that {@link #get}() is efficient - override them if necessary for more efficient computation.
 */
public abstract class AbstractVector implements DataVector {

    protected final int length;
    private double norm = -1;

    protected AbstractVector(int length) {
        this.length = length;
    }

    @Override
    final public int size() {
        return length;
    }

    @Override
    public double norm() {
        if (norm < 0)
            norm = _norm();
        return norm;
    }

    /**
     * Efficiently computes the Euclidean norm of the vector.
     * 
     * @return The vector norm.
     */
    protected double _norm() {
        double sum = 0;
        for (int i = 0; i < length; i++) {
            final double value = get(i);
            sum += value * value;
        }
        return Math.sqrt(sum);
    }

    @Override
    public double sum() {
        double sum = 0;
        for (int i = 0; i < length; i++)
            sum += get(i);
        return sum;
    }

    @Override
    public Iterator<Double> iterator() {
        return new VectorIterative<Double>(length) {
            @Override
            protected Double get(int pos) {
                return AbstractVector.this.get(pos);
            }
        };
    }

    @Override
    public double dot(DataVector vector) {
        double sum = 0;
        int i = 0;
        for (double value : vector) {
            sum += value * get(i++);
        }
        return sum;
    }

    public void addTo(AddableVector vector) {
        for (int i = 0; i < length; i++)
            vector.add(i, get(i));
    }

    public void subtractFrom(SubtractableVector vector) {
        for (int i = 0; i < length; i++)
            vector.subtract(i, get(i));
    }

}
