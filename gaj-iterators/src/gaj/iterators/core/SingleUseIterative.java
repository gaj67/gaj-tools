package gaj.iterators.core;

/**
 * Indicates that the iterative can only be reliably iterated over once.
 * Further iterations should either see an empty sequence or throw an exception.
 */
public interface SingleUseIterative<T> extends Iterative<T> {

}
