package gaj.analysis.numeric.vector;

import java.util.function.Function;

/**
 * Indicates a vector that can have each element modified by a function of the
 * element value.
 */
public interface FunctionableVector extends DataVector {

    /**
     * Applies the given function to each element, replacing the element value.
     * 
     * @param func
     *            - The function to apply.
     */
    void apply(Function<Double, Double> func);

}
