/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serial;

import gaj.config.annotations.Singleton;

/**
 * Provides a Serialiser for Integer objects. Does not add type information.
 */
@Singleton
/*package-private*/ class IntegerSerialiser extends ConfigurableSerialiser<Integer> {

	/*package-private*/ IntegerSerialiser() {
		super();
	}

	/*package-private*/ IntegerSerialiser(SerialiserConfig config) {
		super(config);
	}

	@Override
	public String serialise(Integer obj) throws InvalidSerialisationException {
		return (obj == null) ? super.serialise(obj) : obj.toString();
	}

	@Override
	public Integer deserialise(String data) throws InvalidSerialisationException {
		if (isNull(data)) return super.deserialise(data);
		try {
			return Integer.valueOf(data);
		} catch (RuntimeException e) {
			throw new InvalidSerialisationException("Invalid Integer data: " + data);
		}
	}

}