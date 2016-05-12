/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serialisers;

import gaj.config.annotations.Singleton;

/**
 * Provides a Serialiser for Integer objects. Does not add type information.
 */
@Singleton
/*package-private*/ class IntegerSerialiser extends BaseSerialiser<Integer> {

	/*package-private*/ IntegerSerialiser(/*@Nullable*/ String nullMarker) {
		super(nullMarker);
	}

	@Override
	public /*@Nullable*/ Integer deserialise(/*@Nullable*/ String data) {
		if (isNull(data)) return null;
		try {
			return Integer.valueOf(data);
		} catch (RuntimeException e) {
			throw failure("Invalid Integer data: " + data);
		}
	}

}