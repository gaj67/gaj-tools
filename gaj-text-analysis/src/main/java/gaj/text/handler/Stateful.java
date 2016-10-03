package gaj.text.handler;

/**
 * Specifies an object that maintains mutable state.
 * 
 * @param S - The type of state.
 */
public interface Stateful<S> extends StateGetter<S>, StateSetter<S> {

}
