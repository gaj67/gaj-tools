/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serial.single;

import gaj.config.annotations.Singleton;

/**
 * Provides a Serialiser for String objects. Does not add type information,
 * except in special cases where disambiguation is required between the
 * unserialised and serialised strings.
 */
@Singleton
/*package-private*/ class StringSerialiser extends BaseSerialiser<String> {

	private String LITERAL_NULL_MARKER;

	/*package-private*/ StringSerialiser(String nullMarker) {
		super(nullMarker);
	}

	@Override
	public String serialise(/*@Nullable*/ String obj) {
		return (obj == null) ? nullMarker
				: (obj.equals(nullMarker) || obj.endsWith(LITERAL_NULL_MARKER))
					? (obj + LITERAL_NULL_MARKER) : obj;
	}

	@Override
	public /*@Nullable*/ String deserialise(/*@Nullable*/ String data) {
		return isNull(data) ? null
				: !data.endsWith(LITERAL_NULL_MARKER) ? data
						: data.substring(0, data.length() - LITERAL_NULL_MARKER.length());
	}

}