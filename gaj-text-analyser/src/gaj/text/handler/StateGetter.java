package gaj.text.handler;

/**
 * Obtains the state of an object, which might or might not preserve the state
 * history.
 *
 * @param S - The type of state.
 */
public interface StateGetter<S> {

    /**
     * Obtains the state value to use whenever the value of a requested state is
     * not defined.
     *
     * @return The (possibly null) state to use as the null-state.
     */
    /*@Nullable*/ S nullState();

    /**
     * Obtains the current state.
     *
     * @return The current state, or the null-state if no state has been set.
     */
    /*@Nullable*/ S getState();

    /**
     * Obtains the previous state, if one is being maintained.
     *
     * @return The previous state, or the null-state if no previous state was set.
     */
    /*@Nullable*/ S getPreviousState();

    /**
     * Obtains the parent state (which might also be the previous state), if one
     * is being maintained.
     *
     * @return The parent state, or the null-state if no parent state was set.
     */
    /*@Nullable*/ S getParentState();

    /**
     * Obtains the number of ancestral states in the state history (if any),
     * other than the current state.
     * May be used an an index into {@link #getAncestralState}().
     *
     * @return The number of ancestral states.
     */
    int numAncestralStates();

    /**
     * Obtains the specified ancestral state in the state history, if this is
     * being maintained.
     *
     * @param index - Specifies the index of the state history:
     *            0=current;
     *            1=parent;
     *            2=grandparent; etc. A value of null will be returned for a
     *            negative index.
     * @return The ancestral state, or the null-state if no ancestral state was
     *         set.
     */
    /*@Nullable*/ S getAncestralState(int index);

    /**
     * Indicates whether or not the state of this object matches the state of
     * the given object.
     * If this object maintains historical states, then these will also be
     * checked.
     * Note that a truly null state in this object will be treated as a
     * wild-card for the purposes of matching.
     *
     * @param stateGetter
     *            - The stateful object to be compared.
     * @return A value of true (or false) if the state of two objects do (or do
     *         not) match.
     */
    boolean matches(StateGetter<S> stateGetter);

}
