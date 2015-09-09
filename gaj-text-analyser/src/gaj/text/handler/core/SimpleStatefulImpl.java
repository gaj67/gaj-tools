package gaj.text.handler.core;

import gaj.text.handler.StateGetter;
import gaj.text.handler.Stateful;

/**
 * Implements a simple stateful object that does NOT preserve state history,
 * other than the previous state for convenience.
 *
 * @param S - The type of state.
 */
/*package-private*/ class SimpleStatefulImpl<S> implements Stateful<S> {

    private S state = nullState();
    private S prevState = nullState();

    protected SimpleStatefulImpl() {}

    protected SimpleStatefulImpl(/*@Nullable*/ S state) {
        setState(state);
    }

    protected SimpleStatefulImpl(/*@Nullable*/ S previousState, /*@Nullable*/ S state) {
        setState(previousState);
        setState(state);
    }

    @Override
    public /*@Nullable*/ S nullState() {
        return null;
    }

    @Override
    public S getState() {
        return state;
    }

    @Override
    public S getPreviousState() {
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
    public void setState(/*@Nullable*/ S state) {
        prevState = this.state;
        this.state = toState(state);
    }

    protected /*@Nullable*/ S toState(/*@Nullable*/ S state) {
        return (state == null) ? nullState() : state;
    }

    @Override
    public void rewindState() {
        throw new IllegalStateException("Cannot rewind - state history is not being maintained");
    }

    @Override
    public boolean matches(StateGetter<S> stateGetter) {
        return matchesState(getState(), stateGetter.getState())
                && matchesState(getPreviousState(), stateGetter.getPreviousState());
    }

    protected boolean matchesState(/*@Nullable*/ S myState, S theirState) {
        return myState == null || myState.equals(theirState);
    }

}
