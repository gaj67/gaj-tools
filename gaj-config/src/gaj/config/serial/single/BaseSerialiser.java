/*
 * (c) Geoff Jarrad, 2016.
 */
package gaj.config.serial.single;

/**
 * Specifies the base class used to serialise a known object to a string,
 * and to deserialise such a string back to an object.
 * An instance of this class can either be configured directly on
 * explicit instantiation, or indirectly by implicit instantiation
 * via a SerialiserManager.
 */
/*package-private*/ class BaseSerialiser<T> implements Serialiser<T> {

	protected final String nullMarker;
	
	protected BaseSerialiser(String nullMarker) {
		this.nullMarker = nullMarker;
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
	public String serialise(/*@Nullable*/ T obj) {
		if (obj == null) return nullMarker;
		throw failure("Unknown object: " + obj);
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
	public boolean isNull(/*@Nullable*/ String data) {
		return (data == null || data.equals(nullMarker));
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
	public /*@Nullable*/ T deserialise(/*@Nullable*/ String data) {
		if (isNull(data)) return null;
		throw failure("Unknown serialisation: " + data);
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