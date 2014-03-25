package afl.features;
/**
 * Provides access to features that encode
 * information as an array of numbers.
 */
public interface FeatureVector {

	/**
	 * @return True (or false) if the underlying
	 * feature representation is (or is not) sparse.
	 */
	boolean isSparse();
	/**
	 * @return The full length of the feature vector.
	 */
	int length();
	/**
	 * @param index - The desired position of the particular numerical feature.
	 * @return The feature value.
	 */
	double get(int index);
	/**
	 * @return A dense array of features.
	 */
	double[] dense();
	/**
	 *
	 * @param multiplier - The value by which to scale the features.
	 * @return A scaled version of the feature vector.
	 */
	FeatureVector scale(double multiplier);
	/**
	 * @return The dot product of two feature vectors.
	 */
	double dot(FeatureVector vector);
	/**
	 * @param features - The feature vector to subtract from the
	 * current vector.
	 * @return The difference between two feature vectors.
	 */
	FeatureVector subtract(FeatureVector features);
	/**
	 * @param features - The feature vector to add.
    * @return The sum of two feature vectors.
	 */
   FeatureVector add(FeatureVector features);

}
