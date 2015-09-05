package gaj.text.freedictionary;

/**
 * A state transition rule for a hierarchical, stateful event handler.
 */
public interface HierarchicalStatefulRule<T> extends StatefulRule<T> {

	/**
	 * The rule (partially) matches if this state is non-null and matches the current state.
	 * @return The state to match, or a value of null to ignore this match.
	 */
	/*@Nullable*/ T getParentState();

}
