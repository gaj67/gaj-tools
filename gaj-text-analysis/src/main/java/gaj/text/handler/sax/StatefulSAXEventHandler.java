package gaj.text.handler.sax;

import gaj.text.handler.StateGetter;
import gaj.text.handler.Stateful;



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

    @Override
    public S getParentState() {
        return nullState();
    }

    @Override
    public int numAncestralStates() {
        return 0;
    }

    @Override
    public S getAncestralState(int index) {
        return (index == 0) ? getState() : nullState();
    }

    @Override
    public void rewindState() {
        throw new IllegalStateException("Cannot rewind - state history is not being maintained");
    }

    @Override
    public boolean matches(StateGetter<S> stateGetter) {
        return matchesState(getState(), stateGetter.getState()) && matchesState(getPreviousState(), stateGetter.getPreviousState());
    }

    protected boolean matchesState(/* @Nullable */S myState, S theirState) {
        return myState == null || myState.equals(theirState);
    }

}
