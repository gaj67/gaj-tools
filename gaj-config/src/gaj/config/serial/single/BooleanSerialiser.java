/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serial.single;

import gaj.config.annotations.Singleton;

/**
 * Provides a Serialiser for Boolean objects. Does not add type information.
 */
@Singleton
/*package-private*/ class BooleanSerialiser extends ConfigurableSerialiser<Boolean> {

	/*package-private*/ BooleanSerialiser() {
		super();
	}

	/*package-private*/ BooleanSerialiser(SerialiserConfig config) {
		super(config);
	}

	@Override
	public String serialise(/*@Nullable*/ Boolean obj) {
		return (obj == null) ? config.getNullMarker() : obj.toString();
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