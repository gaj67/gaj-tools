package gaj.analysis.bayes;

import gaj.analysis.bayes.pdf.UnivariatePDF;
import gaj.analysis.bayes.pmf.EmpiricalPMF;

/**
 * Represents a modifiable version of a probability density function (see
 * {@link UnivariatePDF}), discretised as a probability mass function (see
 * {@link EmpiricalPMF}).
 */
public interface EmpiricalPDF extends UnivariatePDF, EmpiricalPMF {

    /**
     * Obtains the number of distinct PDF elements that partition the domain [L,
     * U].
     * 
     * @return The number of elements.
     */
    @Override
    int size();

    /**
     * Obtains the discrete step-size in x values from one PDF element to the
     * next.
     * 
     * @return The step size.
     */
    double step();

    /**
     * Sets the probability density, f(x), of the element corresponding to the
     * given x value. <br/>
     * Warning: Other x values will also map to this same element.
     * 
     * @param x
     *            - The x value.
     * @param value
     *            - The probability density, f(x).
     */
    void set(double x, double value);

    /**
     * Increments the probability density, f(x), of the element corresponding to
     * the given x value. <br/>
     * Warning: Other x values will also map to this same element.
     * 
     * @param x
     *            - The x value.
     * @param value
     *            - The incremental probability density value, delta f(x).
     */
    void add(double x, double value);

    /**
     * Scales the probability density, f(x), of the element corresponding to the
     * given x value. <br/>
     * Warning: Other x values will also map to this same element.
     * 
     * @param x
     *            - The x value.
     * @param value
     *            - The probability density scaling value.
     */
    void mult(double x, double value);

    /**
     * Normalises the probability density values across all elements.
     */
    @Override
    void normalise();

}
