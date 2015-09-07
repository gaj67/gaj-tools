package gaj.text.handler;

/**
 * Specifies an action to be performed.
 */
public interface Action<T> {

	/**
	 * Performs the action with the given context.
	 * 
	 * @param context - The context with which to perform the action.
	 */
	void perform(T context);
	
}
