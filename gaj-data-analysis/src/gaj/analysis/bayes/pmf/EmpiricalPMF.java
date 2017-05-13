package gaj.analysis.bayes.pmf;

/**
 * Represents a finite and modifiable probability mass function (see
 * {@link UnivariatePMF}).
 */
public interface EmpiricalPMF extends UnivariatePMF {

    /**
     * Sets the probability mass, f(x_i), of the specified element, x_i, for i
     * in {start, start+1, ..., end-1, end}. This method has no effect if i <
     * start or i > end. The resulting value may be unnormalised.
     * 
     * @param index
     *            - The index, i, of the element x_i.
     * @param value
     *            - The probability value, f(x_i).
     */
    void set(int index, double value);

    /**
     * Increments the probability mass, f(x_i), of the specified element, x_i,
     * for i in {start, start+1, ..., end-1, end}. This method has no effect if
     * i < start or i > end. The resulting value may be unnormalised.
     * 
     * @param index
     *            - The index, i, of the element x_i.
     * @param value
     *            - The incremental probability value.
     */
    void add(int index, double value);

    /**
     * Scales the probability mass, f(x_i), of the specified element, x_i, for i
     * in {start, start+1, ..., end-1, end}. This method has no effect if i <
     * start or i > end. The resulting value may be unnormalised.
     * 
     * @param index
     *            - The index, i, of the element x_i.
     * @param value
     *            - The probability scaling value.
     */
    void mult(int index, double value);

    /**
     * Normalises the probability mass values across all elements, such that
     * sum_{x in X} f(x) = 1. <br/>
     */
    void normalise();

}
