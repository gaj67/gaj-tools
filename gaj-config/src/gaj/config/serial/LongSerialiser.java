/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serial;

/**
 * Provides a Serialiser for Long objects. Does not add type information.
 */
/*package-private*/ class LongSerialiser extends ConfigurableSerialiser<Long> {

	/*package-private*/ LongSerialiser() {
		super();
	}

	/*package-private*/ LongSerialiser(SerialiserConfig config) {
		super(config);
	}

	@Override
	public String serialise(Long obj) throws InvalidSerialisationException {
		return (obj == null) ? super.serialise(obj) : obj.toString();
	}

	@Override
	public Long deserialise(String data) throws InvalidSerialisationException {
		if (isNull(data)) return super.deserialise(data);
		try {
			return Long.valueOf(data);
		} catch (RuntimeException e) {
			throw new InvalidSerialisationException("Invalid Long data: " + data);
		}
	}

}