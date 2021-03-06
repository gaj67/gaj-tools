package gaj.text.handler.core;

import gaj.text.handler.StateGetter;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements an obtain that maintains a mutable history of states.
 *
 * @param <S> - The type of state.
 */
/*package-private*/ class ContextStatefulImpl<S> extends SimpleStatefulImpl<S> {

    private final List<S> ancestors = new ArrayList<>();

    protected ContextStatefulImpl() {
        super();
    }

    protected ContextStatefulImpl(/*@Nullable*/ S state) {
        super(state);
    }

    protected ContextStatefulImpl(/*@Nullable*/ S previouState, /*@Nullable*/ S state) {
        super(previouState, state);
    }

    @SafeVarargs
    protected ContextStatefulImpl(/*@Nullable*/ S previouState, /*@Nullable*/ S state,
            /*@Nullable*/ S parentState, S/*@Nullable*/... ancestralStates)
    {
        super(previouState, state);
        for (int i = ancestralStates.length - 1; i >= 0; i--) {
            ancestors.add(toState(ancestralStates[i]));
        }
        ancestors.add(toState(parentState));
    }

	@Override
    public void setState(/*@Nullable*/ S state) {
        ancestors.add(getState());
        super.setState(state);
    }

    @Override
    public /*@Nullable*/ S getParentState() {
        return ancestors.isEmpty() ? nullState() : ancestors.get(ancestors.size() - 1);
    }

    @Override
    public void rewindState() {
        S parentState = ancestors.isEmpty() ? nullState() : ancestors.remove(ancestors.size() - 1);
        super.setState(parentState);
    }

    @Override
    public int numAncestralStates() {
        return ancestors.size();
    }

    @Override
    public /*@Nullable*/ S getAncestralState(int index) {
        return (index < 0 || index > ancestors.size()) ? nullState()
                : (index == 0) ? getState() : ancestors.get(ancestors.size() - index);
    }

    @SuppressWarnings("null")
    @Override
    public /*@Nullable*/ S nullState() {
        return null;
    }

    @Override
    public boolean matches(StateGetter<S> stateGetter) {
        return super.matches(stateGetter) && matchesAncestralStates(stateGetter);
    }

    private boolean matchesAncestralStates(StateGetter<S> stateGetter) {
        final int numStates = ancestors.size();
        for (int i = 1; i <= numStates; i++) {
            if (!matchesState(ancestors.get(numStates - i), stateGetter.getAncestralState(i))) {
                return false;
            }
        }
        return true;
    }

}
