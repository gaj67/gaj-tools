package gaj.analysis.bayes;

/**
 * Discretised version of a probability density or mass function.
 */
public interface MutablePDF {

    /**
     * @return The number of distinct PDF elements.
     */
    int size();

    /**
     * Obtains the probability density or mass of the specified element.
     * 
     * @param index
     *            - The index of the element, starting from zero.
     * @return The probability value.
     */
    double get(int index);

    /**
     * Sets the (possibly unnormalised) probability density or mass of the
     * specified element.
     * 
     * @param index
     *            - The index of the element, starting from zero.
     * @param value
     *            - The probability value.
     */
    void set(int index, double value);

    /**
     * Adds the given value to the probability density or mass of the specified
     * element. The result will probably be unnormalised.
     * 
     * @param index
     *            - The index of the element, starting from zero.
     * @param value
     *            - The incremental probability value.
     */
    void add(int index, double value);

    /**
     * Multiplies the probability density or mass of the specified element by
     * the given value. The result will probably be unnormalised.
     * 
     * @param index
     *            - The index of the element, starting from zero.
     * @param value
     *            - The probability scaling value.
     */
    void mult(int index, double value);

    /**
     * Normalises the probability denisty or mass values across all elements.
     */
    void normalise();

}
