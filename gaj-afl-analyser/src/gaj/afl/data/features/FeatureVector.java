package gaj.afl.data.features;

import java.util.Iterator;


/**
 * Provides access to features that encode
 * information as a vector of numbers.
 */
public interface FeatureVector extends Iterable<Double> {

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

	/**
	 * Allows iteration over all elements of the feature vector.
	 * 
	 * @return An element iterator.
	 */
	Iterator<Double> iterator();

}
