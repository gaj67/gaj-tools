package gaj.text.handler;

public abstract class StatefulFactory {

    private StatefulFactory() {}

    public static <S> StateGetter<S> newStateGetter(/*@Nullable*/ S state) {
        return new SimpleStatefulImpl<S>(state);
    }

    public static <S> StateGetter<S> newStateGetter(final /*@Nullable*/ S previousState, final /*@Nullable*/ S state) {
        return new SimpleStatefulImpl<S>(previousState, state);
    }

    @SafeVarargs
    public static <S> StateGetter<S> newStateGetter(/*@Nullable*/ S previousState, /*@Nullable*/ S state, /*@Nulable*/ S parentState, S/*@Nullable*/... ancestralStates) {
        return new ContextStatefulImpl<S>(previousState, state, parentState, ancestralStates);
    }

    public static <S> StateTransition<S> newStateTransition(
            final /*@Nullable*/ S transitionState,
            final /*@Nullable*/ Action preTransitionAction,
            final /*@Nullable*/ Action postTransitionAction)
    {
        return new StateTransition<S>() {
            @Override
            public /*@Nullable*/ S getTransitionState() {
                return transitionState;
            }

            @Override
            public /*@Nullable*/ Action getPreTransitionAction() {
                return preTransitionAction;
            }

            @Override
            public /*@Nullable*/ Action getPostTransitionAction() {
                return postTransitionAction;
            }
        };
    }

    public static <S> StateTransition<S> newStateTransition(
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction)
    {
        return newStateTransition(transitionState, null, postTransitionAction);
    }

    public static <S> StateTransition<S> newStateTransition(/*@Nullable*/ S transitionState) {
        return newStateTransition(transitionState, null, null);
    }

    public static <S> StateTransition<S> newStateTransition(/*@Nullable*/ Action postTransitionAction) {
        return newStateTransition(null, null, postTransitionAction);
    }

}
