package gaj.analysis.numeric.impl;

import gaj.analysis.numeric.DataNumeric;
import gaj.analysis.numeric.matrix.DataMatrix;
import gaj.analysis.numeric.matrix.impl.MatrixFactory;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.impl.VectorFactory;

public abstract class Numerics {

    private Numerics() {}

    /**
     * Computes the relevant norm of the data object.
     * 
     * @param data - The numerical data object.
     * @return The norm.
     */
    public static double norm(DataNumeric data) {
        if (data instanceof DataVector)
            return ((DataVector) data).norm();
        if (data instanceof DataMatrix)
            return ((DataMatrix) data).norm();
        throw new IllegalArgumentException("Unhandled data object: " + data);
    }

    /**
     * Scales the data object by a multiplicative factor. The result is a
     * compound object.
     * 
     * @param data
     *            - The data object to be scaled.
     * @param multiplier
     *            - The scaling factor.
     * @return The scaled data object.
     */
    public static DataNumeric scale(DataNumeric data, double multiplier) {
        if (data instanceof DataVector)
            return VectorFactory.scale((DataVector) data, multiplier);
        if (data instanceof DataMatrix)
            return MatrixFactory.scale((DataMatrix) data, multiplier);
        throw new IllegalArgumentException("Unhandled data object: " + data);
    }

    public static DataNumeric add(DataNumeric obj1, DataNumeric obj2) {
        if (obj1 instanceof DataVector && obj2 instanceof DataVector)
            return VectorFactory.add((DataVector) obj1, (DataVector) obj2);
        if (obj1 instanceof DataMatrix && obj2 instanceof DataMatrix)
            return MatrixFactory.add((DataMatrix) obj1, (DataMatrix) obj2);
        throw new IllegalArgumentException("Unhandled data objects: " + obj1 + ", " + obj2);
    }

    /**
     * Computes the scalar product of two data objects.
     * 
     * @param obj1
     * @param obj2
     * @return The scalar product.
     */
    public static double dot(DataNumeric obj1, DataNumeric obj2) {
        if (obj1 instanceof DataVector && obj2 instanceof DataVector)
            return VectorFactory.dot((DataVector) obj1, (DataVector) obj2);
        if (obj1 instanceof DataMatrix && obj2 instanceof DataMatrix)
            return MatrixFactory.dot((DataMatrix) obj1, (DataMatrix) obj2);
        throw new IllegalArgumentException("Unhandled data objects: " + obj1 + ", " + obj2);
    }

    public static void display(String prefix, DataNumeric obj, String suffix) {
        if (obj instanceof DataVector)
            VectorFactory.display(prefix, (DataVector) obj, suffix);
        else if (obj instanceof DataMatrix)
            MatrixFactory.display(prefix, (DataMatrix) obj, suffix);
        else
            throw new IllegalArgumentException("Unhandled data object: " + obj);
    }

}
