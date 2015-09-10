package gaj.text.handler;

/**
 * Specifies an action to be performed.
 */
@FunctionalInterface
public interface Action {

	/**
	 * Performs the action.
	 */
	void perform();
	
}
