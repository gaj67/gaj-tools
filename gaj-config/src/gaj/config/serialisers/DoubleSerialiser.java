/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serialisers;

import gaj.config.annotations.Singleton;

/**
 * Provides a Serialiser for Double objects. Does not add type information.
 */
@Singleton
/*package-private*/ class DoubleSerialiser extends BaseSerialiser<Double> {

	/*package-private*/ DoubleSerialiser() {
		super();
	}

	/*package-private*/ DoubleSerialiser(/*@Nullable*/ String nullMarker) {
		super(nullMarker);
	}

	@Override
	public /*@Nullable*/ Double deserialise(/*@Nullable*/ String data) {
		if (isNull(data)) return null;
		try {
			return Double.valueOf(data);
		} catch (RuntimeException e) {
			throw failure("Invalid Double data: " + data);
		}
	}

}