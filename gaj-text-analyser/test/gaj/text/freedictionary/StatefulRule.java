package gaj.text.freedictionary;

import gaj.text.handler.Action;

import java.util.Map;

/**
 * A state transition rule for a stateful event handler.
 */
public interface StatefulRule<T> {

	/**
	 * The rule (partially) matches if this state is non-null and matches the current state.
	 * @return The state to match, or a value of null to ignore this match.
	 */
	/*@Nullable*/ T getState();

	/**
	 * The rule (partially) matches if this state is non-null and matches the previous state.
	 * @return The state to match, or a value of null to ignore this match.
	 */
	/*@Nullable*/ T getPreviousState();

	/**
	 * For a start-element or end-element event,
	 * the rule (partially) matches if this name is non-null and matches the element name.
	 * @return The name to match, or a value of null to ignore this match.
	 */
	/*@Nullable*/ String getQualifiedName();

	/**
	 * For a start-element event,
	 * the rule (partially) matches if this map is non-null and all entries match the
	 * element attributes.
	 * @return The attributes to match, or a value of null to ignore this match.
	 */
	/*@Nullable*/ Map<String,String> getAttributes();

	/**
	 * If the rule triggers, then this is the new state to which to transition.
	 * @return The new state, or a value of null to retain the current state.
	 */
	/*@Nullable*/ T getNewState();
	
	/**
	 * If the rule triggers, then this action will be performed immediately before
	 * the state transition. 
	 * @return An action to perform, or a value of null to not perform any action.
	 */
	/*@Nullable*/ Action getPreTransitionAction();
	
	/**
	 * If the rule triggers, then this action will be performed immediately after
	 * the state transition. 
	 * @return An action to perform, or a value of null to not perform any action.
	 */
	/*@Nullable*/ Action getPostTransitionAction();
	
}
