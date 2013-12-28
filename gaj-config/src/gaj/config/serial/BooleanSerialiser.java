/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serial;

/**
 * Provides a Serialiser for Boolean objects. Does not add type information.
 */
/*package-private*/ class BooleanSerialiser extends ConfigurableSerialiser<Boolean> {

	/*package-private*/ BooleanSerialiser() {
		super();
	}

	/*package-private*/ BooleanSerialiser(SerialiserConfig config) {
		super(config);
	}

	@Override
	public String serialise(Boolean obj) throws InvalidSerialisationException {
		return (obj == null) ? super.serialise(obj) : obj.toString();
	}

	@Override
	public Boolean deserialise(String data) throws InvalidSerialisationException {
		if (isNull(data)) return super.deserialise(data);
		try {
			return Boolean.valueOf(data);
		} catch (RuntimeException e) {
			throw new InvalidSerialisationException("Invalid Boolean data: " + data);
		}
	}

}