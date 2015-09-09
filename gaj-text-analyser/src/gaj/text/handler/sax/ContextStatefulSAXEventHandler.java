package gaj.text.handler.sax;

import gaj.text.handler.StateGetter;
import java.util.ArrayList;
import java.util.List;

/**
 * A SAX event handler that maintains a state history.
 *
 * @param S - The type of state.
 */
public abstract class ContextStatefulSAXEventHandler<S> extends StatefulSAXEventHandler<S> {

    private final List<S> ancestors = new ArrayList<>();

    protected ContextStatefulSAXEventHandler() {}

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
