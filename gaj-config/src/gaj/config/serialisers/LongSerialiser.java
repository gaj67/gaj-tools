/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serialisers;

import gaj.config.annotations.Singleton;

/**
 * Provides a Serialiser for Long objects. Does not add type information.
 */
@Singleton
/*package-private*/ class LongSerialiser extends BaseSerialiser<Long> {

	/*package-private*/ LongSerialiser() {
		super();
	}

	/*package-private*/ LongSerialiser(/*@Nullable*/ String nullMarker) {
		super(nullMarker);
	}

	@Override
	public /*@Nullable*/ Long deserialise(/*@Nullable*/ String data) {
		if (isNull(data)) return null;
		try {
			return Long.valueOf(data);
		} catch (RuntimeException e) {
			throw failure("Invalid Long data: " + data);
		}
	}

}