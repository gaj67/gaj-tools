package gaj.text.handler;


/**
 * A state transition rule for a stateful event handler.
 */
public interface StateTransition<S,T> {

	/**
	 * The new state to which to transition.
	 * 
	 * @return The new state, or a value of null to retain the current state.
	 */
	/*@Nullable*/ S getTransitionState();
	
	/**
	 * This action will be performed immediately before
	 * the state transition.
	 *  
	 * @return An action to perform, or a value of null to not perform any action.
	 */
	/*@Nullable*/ Action<T> getPreTransitionAction();
	
	/**
	 * This action will be performed immediately after
	 * the state transition.
	 *  
	 * @return An action to perform, or a value of null to not perform any action.
	 */
	/*@Nullable*/ Action<T> getPostTransitionAction();
	
}
