package gaj.text.freedictionary;

import org.xml.sax.helpers.DefaultHandler;

/**
 * A SAX event handler that maintains the current state and, for convenience, the previous state.
 */
public abstract class StatefulHandler<T> extends DefaultHandler {

	private T curState = null;
	private T prevState = null;
	
	/**
	 * Obtains the current state of the handler.
	 * 
	 * @return The state. 
	 */
	protected /*@Nullable*/ T getState() {
		return curState;
	}

	/**
	 * Sets the current state of the handler.
	 * 
	 * @param state - The new state.
	 */
	protected void setState(/*@Nullable*/ T state) {
		prevState = curState;
		curState = state;
	}

	/**
	 * Obtains the current state of the handler.
	 * 
	 * @return The previous state.
	 */
	protected /*@Nullable*/ T getPreviousState() {
		return prevState;
	}

}
