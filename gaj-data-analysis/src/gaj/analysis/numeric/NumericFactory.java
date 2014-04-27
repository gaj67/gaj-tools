package gaj.analysis.numeric;

import gaj.analysis.matrix.MatrixFactory;
import gaj.analysis.vector.VectorFactory;
import gaj.data.matrix.DataMatrix;
import gaj.data.numeric.DataObject;
import gaj.data.vector.DataVector;

public class NumericFactory {

	private NumericFactory() {}

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
		throw new IllegalArgumentException("Unknown data object");
	}

	/**
	 * Scales the data object by a multiplicative factor.
	 * 
	 * @param data - The data object to be scaled.
	 * @param multiplier - The scaling factor.
	 * @return The scaled data object.
	 */
	public static DataObject scale(DataObject data, double multiplier) {
		if (data instanceof DataVector)
			return VectorFactory.scale((DataVector) data, multiplier);
		if (data instanceof DataMatrix)
			return MatrixFactory.scale((DataMatrix) data, multiplier);
		throw new IllegalArgumentException("Unknown data object");
	}

	public static DataObject add(DataObject obj1, DataObject obj2) {
		if (obj1 instanceof DataVector)
			return VectorFactory.add((DataVector) obj1, (DataVector) obj2);
		if (obj1 instanceof DataMatrix)
			return MatrixFactory.add((DataMatrix) obj1, (DataMatrix) obj2);
		throw new IllegalArgumentException("Unknown data object");
	}

	/**
	 * Computes the scalar product of two data objects.
	 * 
	 * @param obj1
	 * @param obj2
	 * @return The scalar product.
	 */
	public static double dot(DataObject obj1, DataObject obj2) {
		if (obj1 instanceof DataVector)
			return VectorFactory.dot((DataVector) obj1, (DataVector) obj2);
		if (obj1 instanceof DataMatrix)
			return MatrixFactory.dot((DataMatrix) obj1, (DataMatrix) obj2);
		throw new IllegalArgumentException("Unknown data object");
	}

}
