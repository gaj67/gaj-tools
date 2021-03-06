/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serialisers;

/**
 * Specifies the interface used to serialise a known object type to a string,
 * and to deserialise such a string back to an object.
 */
public interface Serialiser<T> {

	/**
	 * Serialises object data, or throws an exception if the object
	 * cannot be serialised.
	 *
	 * @param value - The object to be serialised.
	 * @return A string representation of the object data.
	 * @throws InvalidSerialisationException If an error occurs during
	 * serialisation.
	 */
	/*@Nullable*/ String serialise(/*@Nullable*/ T value);

	/**
	 * Indicates whether or not the supplied serialisation data
	 * represents a null object.
	 *
	 * @param data - A string representation of object data.
	 * @return A value of true (or false) if the data is (or is not)
	 * a serialisation of null.
	 */
	boolean isNull(/*@Nullable*/ String data);

	/**
	 * Deserialises object data, or throws an exception if an
	 * object cannot be deserialised.
	 *
	 * @param data - A string representation of object data.
	 * @return A new object initialised from the data.
	 * @throws InvalidSerialisationException If an error occurs during
	 * deserialisation.
	 */
	/*@Nullable*/ T deserialise(/*@Nullable*/ String data);

	default InvalidSerialisationException failure(String message) {
		return new InvalidSerialisationException(message);
	}

}