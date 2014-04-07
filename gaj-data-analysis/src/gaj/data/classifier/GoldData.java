package gaj.data.classifier;

import java.util.Iterator;

/**
 * Represents a collection of data features with
 * known gold-standard, or ground-truth, classifications.
 *
 */
public interface GoldData extends Iterable<GoldDatum> {

	int numClasses();
	int numFeatures();

	/**
	 * Provides an iterator over each gold-standard data point.
	 */
	Iterator<GoldDatum> iterator();

}
