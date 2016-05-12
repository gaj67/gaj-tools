/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serial.single;

import gaj.config.annotations.Singleton;

/**
 * Provides a Serialiser for Float objects. Does not add type information.
 */
@Singleton
/*package-private*/ class FloatSerialiser extends ConfigurableSerialiser<Float> {

	/*package-private*/ FloatSerialiser() {
		super();
	}

	/*package-private*/ FloatSerialiser(SerialiserConfig config) {
		super(config);
	}

	@Override
	public String serialise(/*@Nullable*/ Float obj) {
		return (obj == null) ? config.getNullMarker() : obj.toString();
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