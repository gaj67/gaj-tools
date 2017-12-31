package gaj.analysis.model.score;

import gaj.analysis.model.DataObject;

/**
 * Specifies a single case or point of data in a data set.
 */
public interface DataCase {

    /**
     * Obtains the data element.
     * 
     * @return The data element.
     */
    DataObject getData();

    /**
     * Obtains the index corresponding to the label for the data element.
     * 
     * @return A non-negative label (or category) index, or a negative value to
     *         indicate that the label is missing.
     */
    int getIndex();

    /**
     * Specifies the weight to attach to the data point.
     * 
     * @return The feature weight.
     */
    double getWeight();

}
