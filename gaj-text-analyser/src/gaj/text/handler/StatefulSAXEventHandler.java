package gaj.text.handler;


/**
 * A SAX event handler that maintains the current state and, for convenience, the previous state.
 */
public abstract class StatefulSAXEventHandler<S> extends SAXEventHandler 
		implements StateGetter<S>, StateSetter<S> 
{

	private S curState = nullState();
	private S prevState = nullState();

	protected StatefulSAXEventHandler() {}
	
	/**
	 * May be used to prevent null values.
	 * 
	 * @return The (possibly null) state corresponding to null.
	 */
	protected /*@Nullable*/ S nullState() {
		return null;
	}
	
	@Override
	public /*@Nullable*/ S getState() {
		return curState;
	}

	@Override
	public void setState(/*@Nullable*/ S state) {
		prevState = curState;
		curState = (state == null) ? nullState() : state;
	}

	@Override
	public /*@Nullable*/ S getPreviousState() {
		return prevState;
	}

}
