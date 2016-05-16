/*
 * (c) Geoff Jarrad, 2016.
 */
package gaj.config.serialisers;



/**
 * Specifies the type information markers used by all Serialiser implementations.
 */
/*package-private*/ class MultiSerialiserConfigImpl implements MultiSerialiserConfig {

	private String typePrefix = "<";
	private String typeSuffix = ">";
	private String nullMarker = "null";

	/*package-private*/ MultiSerialiserConfigImpl() {}

	/*package-private*/ MultiSerialiserConfigImpl(Object... config) {
		final int len = config.length;
		if (len % 2 != 0)
			throw new IllegalArgumentException("Mismatched key/value pairing");
		TypeMarker key = null;
		for (int i = 0; i < len; i++) {
			if (i % 2 == 0) { // Key.
				if (!(config[i] instanceof TypeMarker))
					throw new IllegalArgumentException("Expected TypeMarker key, got " + config[i]);
				key = (TypeMarker) config[i];
			} else { // Value.
				if (!(config[i] instanceof String))
					throw new IllegalArgumentException("Expected String value, got " + config[i]);
				switch (key) {
					case Null:
						nullMarker = (String) config[i];
						break;
					case Prefix:
						typePrefix = (String) config[i];
						break;
					case Suffix:
						typeSuffix = (String) config[i];
						break;
					default:
						throw new IllegalArgumentException("Unhandled Marker key: " + key);
				}
			}
		}
	}

	@Override
	public String getTypePrefix() {
		return typePrefix;
	}

	/*package-private*/ void setTypePrefix(String typePrefix) {
		this.typePrefix = typePrefix;
	}

	@Override
	public String getTypeSuffix() {
		return typeSuffix;
	}

	/*package-private*/ void setTypeSuffix(String typeSuffix) {
		this.typeSuffix = typeSuffix;
	}

	@Override
	public String getNullMarker() {
		return nullMarker;
	}

	/*package-private*/ void setNullMarker(String nullMarker) {
		this.nullMarker = nullMarker;
	}

}
