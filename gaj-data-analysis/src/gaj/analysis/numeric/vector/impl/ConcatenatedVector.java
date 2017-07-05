package gaj.analysis.numeric.vector.impl;

import java.util.Iterator;
import gaj.analysis.numeric.vector.AddableVector;
import gaj.analysis.numeric.vector.CompoundVector;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.IterableVector;
import gaj.common.annotations.PackagePrivate;

/**
 * Implements the (deferred) concatenation of multiple data vectors together into a single, compound vector.
 */
@PackagePrivate class ConcatenatedVector extends AbstractVector implements CompoundVector {

    private final DataVector[] vectors;

    @PackagePrivate ConcatenatedVector(DataVector[] vectors) {
        super(computeLength(vectors));
        this.vectors = vectors;
    }

    private static int computeLength(DataVector[] vectors) {
        int length = 0;
        for (DataVector vector : vectors)
            length += vector.size();
        return length;
    }

    @Override
    public double get(final int pos) {
        if (pos >= 0 && pos < length) {
            int _pos = pos;
            for (DataVector vector : vectors) {
                if (_pos < vector.size())
                    return vector.get(_pos);
                _pos -= vector.size();
            }
        }
        throw new IndexOutOfBoundsException("Bad index: " + pos);
    }

    @Override
    public Iterator<Double> iterator() {
        return new VectorIterative<Double>(length) {
            private Iterator<Double> iter = null;
            private int i = 0;

            @Override
            public boolean hasNext() {
                if (i >= vectors.length)
                    return false;
                if (iter == null)
                    iter = getIterator(vectors[0]);
                while (!iter.hasNext() && ++i < vectors.length)
                    iter = getIterator(vectors[i]);
                return (i < vectors.length);
            }

            @Override
            protected Double get(int pos) {
                return iter.next();
            }
        };
    }

    private Iterator<Double> getIterator(DataVector vector) {
        if (vector instanceof IterableVector) {
            return ((IterableVector) vector).iterator();
        }
        final int length = vector.size();
        return new VectorIterative<Double>(length) {
            @Override
            protected Double get(int pos) {
                return vector.get(pos);
            }
        };
    }

    @Override
    protected double _norm() {
        double sum = 0;
        for (DataVector vector : vectors) {
            double norm = vector.norm();
            sum += norm * norm;
        }
        return Math.sqrt(sum);
    }

    @Override
    public double sum() {
        double sum = 0;
        for (DataVector vector : vectors)
            sum += vector.sum();
        return sum;
    }

    @Override
    public double dot(DataVector vector) {
        double sum = 0;
        int pos = 0;
        for (DataVector vector1 : vectors) {
            DataVector vector2 = new SubVector(vector, pos, vector1.size());
            sum += vector1.dot(vector2);
            pos += vector1.size();
        }
        return sum;
    }

    @Override
    public void addTo(AddableVector vector) {
        int pos = 0;
        for (DataVector vector1 : vectors) {
            if (vector1 instanceof AbstractVector) {
                ((AbstractVector) vector1).addTo(new AddableSubVector(vector, pos, vector1.size()));
                pos += vector1.size();
            } else {
                final int length = vector1.size();
                for (int i = 0; i < length; i++) {
                    vector.add(pos++, vector1.get(i));
                }
            }
        }
    }

}
