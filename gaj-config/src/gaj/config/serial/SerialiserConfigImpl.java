/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serial;

/**
 * Specifies the type information markers used by all Serialiser implementations.
 */
/*package-private*/ class SerialiserConfigImpl implements SerialiserConfig {
	private String typePrefix = "<";
	private String typeSuffix = ">";
	private String nullMarker = "null";

	/*package-private*/ SerialiserConfigImpl() {}

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
