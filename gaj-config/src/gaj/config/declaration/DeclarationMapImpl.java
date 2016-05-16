package gaj.config.declaration;

import java.util.Collections;
import java.util.Map;

/*package-private*/ class DeclarationMapImpl implements DeclarationMap {

	private final Map<String, Declaration> map;

	/*package-private*/ DeclarationMapImpl(Map<String/*key-name*/, Declaration> map) {
		this.map = map;
	}
	
	@Override
	public int numProperties() {
		return map.size();
	}

	@Override
	public Iterable<String> getKeys() {
		return Collections.unmodifiableCollection(map.keySet());
	}

	@Override
	public /*@Nullable*/ Declaration getDeclaration(String key) {
		return map.get(key);
	}

	@Override
	public Iterable<Declaration> getDeclarations() {
		return Collections.unmodifiableCollection(map.values());
	}

}
