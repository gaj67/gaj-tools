/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serial.single;

import gaj.config.annotations.Singleton;

/**
 * Provides a Serialiser for Short objects. Does not add type information.
 */
@Singleton
/*package-private*/ class ShortSerialiser extends ConfigurableSerialiser<Short> {

	/*package-private*/ ShortSerialiser() {
		super();
	}

	/*package-private*/ ShortSerialiser(SerialiserConfig config) {
		super(config);
	}

	@Override
	public String serialise(/*@Nullable*/ Short obj) {
		return (obj == null) ? config.getNullMarker() : obj.toString();
	}

	@Override
	public /*@Nullable*/ Short deserialise(/*@Nullable*/ String data) {
		if (isNull(data)) return null;
		try {
			return Short.valueOf(data);
		} catch (RuntimeException e) {
			throw failure("Invalid Short data: " + data);
		}
	}

}