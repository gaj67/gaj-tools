/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serialisers;

import gaj.config.annotations.Singleton;

/**
 * Provides a Serialiser for Boolean objects. Does not add type information.
 */
@Singleton
/*package-private*/ class BooleanSerialiser extends BaseSerialiser<Boolean> {

	/*package-private*/ BooleanSerialiser() {
		super();
	}

	/*package-private*/ BooleanSerialiser(/*@Nullable*/ String nullMarker) {
		super(nullMarker);
	}

	@Override
	public /*@Nullable*/ Boolean deserialise(/*@Nullable*/ String data) {
		if (isNull(data)) return null;
		try {
			return Boolean.valueOf(data);
		} catch (RuntimeException e) {
			throw failure("Invalid Boolean data: " + data);
		}
	}

}