package gaj.text.handler;

/**
 * Obtains the history of ancestral states of an object.
 */
public interface ContextStateGetter<T> extends StateGetter<T> {

	/**
	 * Obtains the parent state, which might also be the previous state.
	 * 
	 * @return The parent state, or a value of null if no parent state was set.
	 */
	/*@Nullable*/ T getParentState();

	/**
	 * Obtains the number of ancestral states in the state history, other than the current state.
	 * May be used an an index into {@link #getAncestralState}().
	 * 
	 * @return The number of ancestral states.
	 */
	int numAncestralStates();
	
	/**
	 * Obtains the specified ancestral state in the state history.
	 * 
	 * @param index - Specifies the index of the state history: 0=current; 1=parent;
	 * 2=grandparent; etc. A value of null will be returned for a negative index. 
	 * @return The ancestral state, or a value of null if no ancestral state was set.
	 */
	/*@Nullable*/ T getAncestralState(int index);

}
