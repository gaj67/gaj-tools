/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serial;

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
	public String serialise(Float obj) throws InvalidSerialisationException {
		return (obj == null) ? super.serialise(obj) : obj.toString();
	}

	@Override
	public Float deserialise(String data) throws InvalidSerialisationException {
		if (isNull(data)) return super.deserialise(data);
		try {
			return Float.valueOf(data);
		} catch (RuntimeException e) {
			throw new InvalidSerialisationException("Invalid Float data: " + data);
		}
	}

}