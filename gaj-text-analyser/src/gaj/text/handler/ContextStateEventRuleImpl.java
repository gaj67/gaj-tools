package gaj.text.handler;

/*package-private*/ class ContextStateEventRuleImpl<S, T, V> 
		extends StateEventRuleImpl<S, T, V> implements ContextStateEventRule<S, T, V> {

	/*package-private*/ ContextStateEventRuleImpl(
			/*@Nullable*/ ContextStateGetter<S> stateGetter,
			/*@Nullable*/ Event<T, V> event, 
			/*@Nullable*/ StateTransition<S> stateTransition) 
	{
		super(stateGetter, event, stateTransition);
	}

	@Override
	public /*@Nullable*/ ContextStateGetter<S> getStateGetter() {
		return (ContextStateGetter<S>) super.getStateGetter();
	}
	
	@Override
	public boolean matches(ContextStateGetter<S> stateGetter, Event<T, V> event) {
		return super.matches(stateGetter, event) 
				&& (getStateGetter() == null || matchesAncestralStates(stateGetter));
	}

	private boolean matchesAncestralStates(ContextStateGetter<S> stateGetter) {
		ContextStateGetter<S> myStateGetter = getStateGetter();
		final int numAncestors = myStateGetter.numAncestralStates();
		for (int i = 1; i <= numAncestors; i++) {
			if (!matchesState(myStateGetter.getAncestralState(i), stateGetter.getAncestralState(i)))
				return false;
		}
		return true;
	}

}
