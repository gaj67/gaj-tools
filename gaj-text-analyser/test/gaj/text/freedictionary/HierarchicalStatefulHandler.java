package gaj.text.freedictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * A SAX event handler that maintains a contextual list of hierarchical states.
 */
public abstract class HierarchicalStatefulHandler<T> extends StatefulHandler<T> {

	private final List<T> ancestors = new ArrayList<>();
	
	@Override
	protected void setState(/*@Nullable*/ T state) {
		ancestors.add(getState());
		super.setState(state);
	}

	/**
	 * Obtains the parent state of the handler.
	 * 
	 * @return The parent state.
	 */
	protected /*@Nullable*/ T getParentState() {
		return ancestors.isEmpty() ? null : ancestors.get(ancestors.size()-1);
	}

	/**
	 * Resets the state of the handler to the parent state.
	 */
	protected void restoreParentState() {
		T parentState = ancestors.isEmpty() ? null : ancestors.remove(ancestors.size()-1);
		super.setState(parentState);
	}

}
