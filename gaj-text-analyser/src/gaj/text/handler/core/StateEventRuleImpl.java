package gaj.text.handler.core;

import gaj.text.handler.Event;
import gaj.text.handler.StateEventRule;
import gaj.text.handler.StateGetter;
import gaj.text.handler.StateTransition;

/*package-private*/class StateEventRuleImpl<S, E extends Event<?, ?>> implements StateEventRule<S, E> {

    private final StateGetter<S> stateGetter;
    private final E event;
    private final StateTransition<S> stateTransition;

    /*package-private*/ StateEventRuleImpl(
            /*@Nullable*/ StateGetter<S> stateGetter,
            /* @Nullable */E event,
            /*@Nullable*/ StateTransition<S> stateTransition)
            {
        this.stateGetter = stateGetter;
        this.event = event;
        this.stateTransition = stateTransition;
            }

    @Override
    public boolean matches(StateGetter<S> stateGetter, E event) {
        return matchesState(stateGetter) && matchesEvent(event);
    }

    protected boolean matchesState(StateGetter<S> stateGetter) {
        return this.stateGetter == null || this.stateGetter.matches(stateGetter);
    }

    protected boolean matchesState(/*@Nullable*/ S ruleState, S handlerState) {
        return ruleState == null || ruleState.equals(handlerState);
    }

    protected boolean matchesEvent(E event) {
        return this.event == null || this.event.matches(event);
    }

    @Override
    public /*@Nullable*/ StateTransition<S> getStateTransition() {
        return stateTransition;
    }

    @Override
    public /*@Nullable*/ StateGetter<S> getStateGetter() {
        return stateGetter;
    }

    @Override
    public/* @Nullable */E getEvent() {
        return event;
    }

}
