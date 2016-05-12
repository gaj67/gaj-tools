/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serial.single;

import gaj.config.annotations.Singleton;

/**
 * Provides a Serialiser for Long objects. Does not add type information.
 */
@Singleton
/*package-private*/ class LongSerialiser extends ConfigurableSerialiser<Long> {

	/*package-private*/ LongSerialiser() {
		super();
	}

	/*package-private*/ LongSerialiser(SerialiserConfig config) {
		super(config);
	}

	@Override
	public String serialise(/*@Nullable*/ Long obj) {
		return (obj == null) ? config.getNullMarker() : obj.toString();
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