package gaj.text.handler;

/**
 * Maintains the state of an object, which might or might not preserve the state
 * history.
 * 
 * @param S - The type of state.
 */
public interface StateSetter<S> {

    /**
     * Sets the current state to the given value. A null value may be converted
     * to a null-state.
     * <p/>
     * If state history is being maintained, then the (old) current state will
     * be moved to the parent state in the history, and will also become the
     * previous state.
     * <p/>
     * History before adding new state Z:
     * 
     * <pre>
     * ... -> X (parent) -> Y (current)
     * </pre>
     * 
     * History after adding new state Z:
     * 
     * <pre>
     * ... -> X (grandparent) -> Y (parent & last) -> Z (current)
     * </pre>
     *
     * @param state
     *            - The new (possibly null) state.
     */
    void setState(/*@Nullable*/ S state);

    /**
     * If state history is being maintained, then the (old) current state
     * will be removed from the history, thereby restoring the parent state
     * as the new current state. The old current state will become the previous
     * state.
     * <p/>
     * History before removing state Z:
     * <pre>
     * ... -> X (grandparent) -> Y (parent) -> Z (current)
     * </pre>
     * History after removing state Z:
     * <pre>
     * ... -> X (parent) -> Y (current); Z (last)
     * </pre>
     */
    void rewindState();

}
