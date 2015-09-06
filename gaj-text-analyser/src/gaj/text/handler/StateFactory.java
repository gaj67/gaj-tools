package gaj.text.handler;

public abstract class StateFactory {

	private StateFactory() {}
	
	public static <S> StateGetter<S> newStateGetter(/*@Nullable*/ S previousState, /*@Nullable*/ S state) {
		return new StateGetter<S>() {
			@Override
			public /*@Nullable*/ S getState() {
				return state;
			}

			@Override
			public /*@Nullable*/ S getPreviousState() {
				return previousState;
			}
		};
	}

	/**
	 * 
	 * @param previousState - Specifies an optional previous state.
	 * @param stateHistory - Specifies an optional state history, in the order
	 * [current, parent, grandparent, ...].
	 * @return A contextual state-getter.
	 */
	public static <S> ContextStateGetter<S> newContextStateGetter(/*@Nullable*/ S previousState, S/*@Nullable*/... stateHistory) {
		return new ContextStateGetter<S>() {
			@Override
			public /*@Nullable*/ S getState() {
				return stateHistory.length == 0 ? null : stateHistory[0];
			}

			@Override
			public /*@Nullable*/ S getPreviousState() {
				return previousState;
			}

			@Override
			public /*@Nullable*/ S getParentState() {
				return stateHistory.length < 2 ? null : stateHistory[1];
			}

			@Override
			public /*@Nullable*/ S getAncestralState(int index) {
				return (index < 0 || index >= stateHistory.length) ? null : stateHistory[index];
			}

			@Override
			public int numAncestralStates() {
				return stateHistory.length > 0 ? stateHistory.length - 1 : 0;
			}
		};
	}

	public static <S> StateTransition<S> newStateTransition(
			/*@Nullable*/ S transitionState, 
			/*@Nullable*/ Action preTransitionAction, 
			/*@Nullable*/ Action postTransitionAction) 
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
}
