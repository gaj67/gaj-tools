/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serial;

/**
 * Specifies the base class used to serialise a known object to a string,
 * and to deserialise such a string back to an object.
 * An instance of this class can either be configured directly on
 * explicit instantiation, or indirectly by implicit instantiation
 * via a SerialiserManager.
 */
public class ConfigurableSerialiser<T> implements Serialiser<T> {

	/**
	 * The immutable configuration.
	 * Set once only either by construction or injection via {@link #configure}().
	 */
	protected SerialiserConfig config;

	/**
	 * Creates an unconfigured instance of the serialiser.
	 * The configuration must subsequently be injected via {@link #configure}().
	 */
	/*package-private*/ ConfigurableSerialiser() {}

	protected ConfigurableSerialiser(SerialiserConfig config) {
		configure(config);
	}

	/*package-private*/ void configure(SerialiserConfig config) {
		if (this.config != null)
			throw new IllegalArgumentException("The serialiser configuration has already been set");
		this.config = config;
	}

	/**
	 * Serialises object data, or throws an exception if the object
	 * cannot be serialised.
	 *
	 * @param obj - The object to be serialised.
	 * @return A string representation of the object data.
	 * @throws InvalidSerialisationException If an error occurs during
	 * serialisation.
	 */
	@Override
	public String serialise(/*@Nullable*/ T obj) throws InvalidSerialisationException {
		if (obj == null) return config.getNullMarker();
		throw new InvalidSerialisationException("Unknown object: " + obj);
	}

	/**
	 * Indicates whether or not the supplied serialisation data
	 * represents a null object.
	 *
	 * @param data - A string representation of object data.
	 * @return A value of true (or false) if the data is (or is not)
	 * a serialisation of null.
	 */
	@Override
	public boolean isNull(String data) {
		return (data == null || data.equals(config.getNullMarker()));
	}

	/**
	 * Deserialises object data, or throws an exception if an
	 * object cannot be deserialised.
	 *
	 * @param data - A string representation of object data.
	 * @return A new object initialised from the data.
	 * @throws InvalidSerialisationException If an error occurs during
	 * deserialisation.
	 */
	@Override
	public /*@Nullable*/ T deserialise(String data) throws InvalidSerialisationException {
		if (isNull(data)) return null;
		throw new InvalidSerialisationException("Unknown serialisation: " + data);
	}

	/**
	 * Clones the current serialiser.
	 *
	 * @return A new instance of a Serialiser, with
	 * a copy of the current configuration.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Serialiser<T> clone() {
		try {
			return (Serialiser<T>) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.getMessage());
		}
	}

}