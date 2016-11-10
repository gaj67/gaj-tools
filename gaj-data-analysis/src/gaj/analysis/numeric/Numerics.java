package gaj.analysis.numeric;

import gaj.analysis.numeric.matrix.DataMatrix;
import gaj.analysis.numeric.matrix.impl.MatrixFactory;
import gaj.analysis.numeric.object.DataObject;
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
    public static double norm(DataObject data) {
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
    public static DataObject scale(DataObject data, double multiplier) {
        if (data instanceof DataVector)
            return VectorFactory.scale((DataVector) data, multiplier);
        if (data instanceof DataMatrix)
            return MatrixFactory.scale((DataMatrix) data, multiplier);
        throw new IllegalArgumentException("Unhandled data object: " + data);
    }

    public static DataObject add(DataObject obj1, DataObject obj2) {
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
    public static double dot(DataObject obj1, DataObject obj2) {
        if (obj1 instanceof DataVector && obj2 instanceof DataVector)
            return VectorFactory.dot((DataVector) obj1, (DataVector) obj2);
        if (obj1 instanceof DataMatrix && obj2 instanceof DataMatrix)
            return MatrixFactory.dot((DataMatrix) obj1, (DataMatrix) obj2);
        throw new IllegalArgumentException("Unhandled data objects: " + obj1 + ", " + obj2);
    }

    public static void display(String prefix, DataObject obj, String suffix) {
        if (obj instanceof DataVector)
            VectorFactory.display(prefix, (DataVector) obj, suffix);
        else if (obj instanceof DataMatrix)
            MatrixFactory.display(prefix, (DataMatrix) obj, suffix);
        else
            throw new IllegalArgumentException("Unhandled data object: " + obj);
    }

}