package gaj.text.handler;


/**
 * A SAX event handler that maintains the current state and, for convenience, the previous state.
 * 
 * @param S - The type of state.
 */
public abstract class StatefulSAXEventHandler<S> extends SAXEventHandler implements Stateful<S> {

    private S curState = nullState();
    private S prevState = nullState();

    protected StatefulSAXEventHandler() {}

    /**
     * May be used to prevent null values.
     *
     * @return The (possibly null) state corresponding to null.
     */
    @Override
    public /*@Nullable*/ S nullState() {
        return null;
    }

    @Override
    public /*@Nullable*/ S getState() {
        return curState;
    }

    @Override
    public void setState(/*@Nullable*/ S state) {
        prevState = curState;
        curState = toState(state);
    }

    protected /*@Nullable*/ S toState(/*@Nullable*/ S state) {
        return (state == null) ? nullState() : state;
    }

    @Override
    public /*@Nullable*/ S getPreviousState() {
        return prevState;
    }

}
