package gaj.analysis.data.classifier;

import java.util.Iterator;

/**
 * Represents a collection of data features with
 * known gold-standard, or ground-truth, classifications.
 *
 */
public interface GoldData extends Iterable<GoldDatum> {

    /**
     * Indicates the total number C of classes in the classification scheme.
     * 
     * @return The number of classes.
     */
    int numClasses();

    /**
     * Indicates the number F of numerical features in any feature vector
     * to be classified.
     * 
     * @return The number of feature elements.
     */
    int numFeatures();

    /**
     * Provides an iterator over each gold-standard data point.
     */
    Iterator<GoldDatum> iterator();

}
