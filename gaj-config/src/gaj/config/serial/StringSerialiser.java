/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serial;

import gaj.config.annotations.Singleton;

/**
 * Provides a Serialiser for String objects. Does not add type information,
 * except in special cases where disambiguation is required between the
 * unserialised and serialised strings.
 */
@Singleton
/*package-private*/ class StringSerialiser extends ConfigurableSerialiser<String> {

	private String LITERAL_NULL_MARKER;

	/*package-private*/ StringSerialiser() {
		super();
	}

	/*package-private*/ StringSerialiser(SerialiserConfig config) {
		super(config);
	}

	@Override
	/*package-private*/ void configure(SerialiserConfig config) {
		super.configure(config);
		LITERAL_NULL_MARKER = config.getTypePrefix() + "string" + config.getTypeSuffix();
	}

	@Override
	public String serialise(String obj) throws InvalidSerialisationException {
		return (obj == null) ? config.getNullMarker()
				: (obj.equals(config.getNullMarker()) || obj.endsWith(LITERAL_NULL_MARKER))
				? (obj + LITERAL_NULL_MARKER)
						: obj;
	}

	@Override
	public String deserialise(String data) throws InvalidSerialisationException {
		return isNull(data) ? null
				: !data.endsWith(LITERAL_NULL_MARKER) ? data
						: data.substring(0, data.length() - LITERAL_NULL_MARKER.length());
	}

}