package gaj.text.handler;

/**
 * Obtains the state of an object.
 */
public interface StateGetter<T> {

	/**
	 * Obtains the current state.
	 * 
	 * @return The current state, or a value of null if no state has been set.
	 */
	/*@Nullable*/ T getState();

	/**
	 * Obtains the previous state.
	 * 
	 * @return The previous state, or a value of null if no previous state was set.
	 */
	/*@Nullable*/ T getPreviousState();

}
