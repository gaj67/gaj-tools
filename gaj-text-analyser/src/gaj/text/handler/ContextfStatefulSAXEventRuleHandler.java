package gaj.text.handler;

import java.util.Collection;

public abstract class ContextfStatefulSAXEventRuleHandler<S> extends ContextStatefulSAXEventHandler<S> {

	protected ContextfStatefulSAXEventRuleHandler() {}
	
	protected abstract Collection<? extends ContextStateEventRule<S,SAXEventType,String>> getRules();

	protected void handleEvent(SAXEvent event) {
		final Collection<? extends ContextStateEventRule<S,SAXEventType,String>> rules = getRules();
		for (ContextStateEventRule<S, SAXEventType, String> rule : rules) {
			if (rule.matches(this, event)) {
				StateTransition<S> transition = rule.getStateTransition();
				if (transition != null) {
					Action action = transition.getPreTransitionAction();
					if (action != null) action.perform();
					S newState = transition.getTransitionState();
					if (newState != null) setState(newState);
					action = transition.getPostTransitionAction();
					if (action != null) action.perform();
				}
				break;
			}
		}
	}

}
