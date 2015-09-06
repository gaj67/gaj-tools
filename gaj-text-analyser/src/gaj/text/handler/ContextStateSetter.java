package gaj.text.handler;

/**
 * Maintains a history of ancestral states of an object.
 */
public interface ContextStateSetter<S> extends StateSetter<S> {

	/**
	 * Moves the current state to the parent state in the history, and sets the current state
	 * to the new state. The old current state will become the previous state.
	 * <p/>History before adding new state Z:
	 * <pre>... -> X (parent) -> Y (current)</pre>
	 * History after adding new state Z:
	 * <pre>... -> X (grandparent) -> Y (parent & last) -> Z (current)</pre>
	 * 
	 * @param state - The new (possibly null) state.
	 */
	@Override
	void setState(/*@Nullable*/ S state);

	/**
	 * Removes the current state from the history, thereby restoring the parent state
	 * as the new current state. The old current state will become the previous state.
	 * <p/>History before removing state Z:
	 * <pre>... -> X (grandparent) -> Y (parent) -> Z (current)</pre>
	 * History after removing state Z:
	 * <pre>... -> X (parent) -> Y (current); Z (last)</pre>
	 */
	void rewindState();

}
