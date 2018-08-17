package gaj.analysis.model.score;

/**
 * Specifies a single case or point of data in a data set.
 * 
 * <I> - The type of input data.
 */
public interface Datum<I> {

    /**
     * Obtains the data element.
     * 
     * @return The data element.
     */
    I getData();

    /**
     * Specifies the weight to attach to the data point.
     * 
     * @return The feature weight.
     */
    double getWeight();

}
