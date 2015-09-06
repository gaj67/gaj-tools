package gaj.text.freedictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gaj.text.handler.Action;
import gaj.text.handler.ContextStateEventRule;
import gaj.text.handler.ContextfStatefulSAXEventRuleHandler;
import gaj.text.handler.RuleFactory;
import gaj.text.handler.SAXEventType;

public class HMSectionRuleHandler extends ContextfStatefulSAXEventRuleHandler<State> {

	@SuppressWarnings("serial")
	private static Map<String,String> HM_SECTION_ATTRS = new HashMap<String,String>() {{
		put("data-src", "hm");
	}};
	
	private List<ContextStateEventRule<State, SAXEventType, String>> rules = null;

	private boolean captureText = false;
	
	@Override
	protected State nullState() {
		return State.INIT;
	}
	
	@Override
	public void setState(State state) {
		if (State.REWIND == state) {
			rewindState();
		} else {
			super.setState(state);
		}
		captureText = getState().isTextual();
	}

	@Override
	protected Collection<? extends ContextStateEventRule<State, SAXEventType, String>> getRules() {
		if (rules == null) {
			rules = new ArrayList<>();
			Action preStateAction = new Action() {
				@Override
				public void perform() {
					System.out.printf("Before: current=%s, parent=%s, previous=%s, capture=%s%n", 
							getState(), getParentState(), getPreviousState(), captureText);
					
				}
			};
			Action postStateAction = new Action() {
				@Override
				public void perform() {
					System.out.printf("After: current=%s, parent=%s, previous=%s, capture=%s%n", 
							getState(), getParentState(), getPreviousState(), captureText);
					
				}
			};
			rules.add(RuleFactory.newRule(
					State.INIT, null, null, 
					SAXEventType.BEGIN_ELEMENT, "section", HM_SECTION_ATTRS, 
					preStateAction, State.SECTION, postStateAction));
			rules.add(RuleFactory.newRule(
					State.SECTION, null, null, 
					SAXEventType.BEGIN_ELEMENT, "h2", null, 
					preStateAction, State.WORD, postStateAction));
			rules.add(RuleFactory.newRule(
					State.WORD, null, null, 
					SAXEventType.END_ELEMENT, "h2", null, 
					preStateAction, State.REWIND, postStateAction));
			rules.add(RuleFactory.newRule(
					State.SECTION, null, null, 
					SAXEventType.END_ELEMENT, "section", null, 
					preStateAction, State.INIT, postStateAction));
		}
		return rules;
	}

}
