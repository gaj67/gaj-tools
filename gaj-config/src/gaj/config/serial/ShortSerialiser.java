/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serial;

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
	public String serialise(Short obj) throws InvalidSerialisationException {
		return (obj == null) ? super.serialise(obj) : obj.toString();
	}

	@Override
	public Short deserialise(String data) throws InvalidSerialisationException {
		if (isNull(data)) return super.deserialise(data);
		try {
			return Short.valueOf(data);
		} catch (RuntimeException e) {
			throw new InvalidSerialisationException("Invalid Short data: " + data);
		}
	}

}