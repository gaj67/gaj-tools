/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serial.single;

import gaj.config.annotations.Singleton;

/**
 * Provides a Serialiser for Double objects. Does not add type information.
 */
@Singleton
/*package-private*/ class DoubleSerialiser extends ConfigurableSerialiser<Double> {

	/*package-private*/ DoubleSerialiser() {
		super();
	}

	/*package-private*/ DoubleSerialiser(SerialiserConfig config) {
		super(config);
	}

	@Override
	public String serialise(/*@Nullable*/ Double obj) {
		return (obj == null) ? config.getNullMarker() : obj.toString();
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