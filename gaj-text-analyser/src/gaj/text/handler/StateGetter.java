package gaj.text.handler;

/**
 * Obtains the state of an object.
 */
public interface StateGetter<S> {

	/**
	 * Obtains the current state.
	 * 
	 * @return The current state, or a value of null if no state has been set.
	 */
	/*@Nullable*/ S getState();

	/**
	 * Obtains the previous state.
	 * 
	 * @return The previous state, or a value of null if no previous state was set.
	 */
	/*@Nullable*/ S getPreviousState();

}
