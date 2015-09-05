package gaj.text.handler;

/**
 * Maintains the state of an object.
 */
public interface StateSetter<T> {

	/**
	 * Sets the current state.
	 * 
	 * @param state - The new (possibly null) state.
	 */
	void setState(/*@Nullable*/ T state);

}
