/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serialisers;

import gaj.config.annotations.Singleton;

/**
 * Provides a Serialiser for String objects. Does not add type information,
 * except in special cases where disambiguation is required between the
 * unserialised and serialised strings.
 */
@Singleton
/*package-private*/ class StringSerialiser extends BaseSerialiser<String> {

	/*package-private*/ StringSerialiser() {
		super();
	}

	/*package-private*/ StringSerialiser(/*@Nullable*/ String nullMarker) {
		super(nullMarker);
	}

	@Override
	public /*@Nullable*/ String deserialise(/*@Nullable*/ String data) {
		return isNull(data) ? null : data;
	}

}