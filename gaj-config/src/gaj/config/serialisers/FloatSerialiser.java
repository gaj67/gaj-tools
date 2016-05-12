/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serialisers;

import gaj.config.annotations.Singleton;

/**
 * Provides a Serialiser for Float objects. Does not add type information.
 */
@Singleton
/*package-private*/ class FloatSerialiser extends BaseSerialiser<Float> {

	/*package-private*/ FloatSerialiser(/*@Nullable*/ String nullMarker) {
		super(nullMarker);
	}

	@Override
	public /*@Nullable*/ Float deserialise(/*@Nullable*/ String data) {
		if (isNull(data)) return null;
		try {
			return Float.valueOf(data);
		} catch (RuntimeException e) {
			throw failure("Invalid Float data: " + data);
		}
	}

}