/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serial;

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
	public String serialise(Double obj) throws InvalidSerialisationException {
		return (obj == null) ? super.serialise(obj) : obj.toString();
	}

	@Override
	public Double deserialise(String data) throws InvalidSerialisationException {
		if (isNull(data)) return super.deserialise(data);
		try {
			return Double.valueOf(data);
		} catch (RuntimeException e) {
			throw new InvalidSerialisationException("Invalid Double data: " + data);
		}
	}

}