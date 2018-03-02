package gaj.analysis.data.numeric.vector;

/**
 * Indicates a data vector with mutable data.
 * Use sparingly, because most methods assume
 * immutable data vectors. In particular, beware
 * of changing data after the vector has been cached.
 */
public interface WritableVector
        extends SettableVector, AddableVector, SubtractableVector, MultiplicableVector, FunctionableVector {

}
