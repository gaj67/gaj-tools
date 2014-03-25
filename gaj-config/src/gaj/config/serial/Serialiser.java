/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serial;

/**
 * Specifies the interface used to serialise a known object type to a string,
 * and to deserialise such a string back to an object.
 */
public interface Serialiser<T> extends Cloneable {

	/**
	 * Serialises object data, or throws an exception if the object
	 * cannot be serialised.
	 *
	 * @param value - The object to be serialised.
	 * @return A string representation of the object data.
	 * @throws InvalidSerialisationException If an error occurs during
	 * serialisation.
	 */
	public String serialise(/*@Nullable*/ T value) throws InvalidSerialisationException;

	/**
	 * Indicates whether or not the supplied serialisation data
	 * represents a null object.
	 *
	 * @param data - A string representation of object data.
	 * @return A value of true (or false) if the data is (or is not)
	 * a serialisation of null.
	 */
	public boolean isNull(/*@Nullable*/ String data);

	/**
	 * Deserialises object data, or throws an exception if an
	 * object cannot be deserialised.
	 *
	 * @param data - A string representation of object data.
	 * @return A new object initialised from the data.
	 * @throws InvalidSerialisationException If an error occurs during
	 * deserialisation.
	 */
	public /*@Nullable*/ T deserialise(/*@Nullable*/ String data) throws InvalidSerialisationException;

	/**
	 * Clones the current serialiser.
	 *
	 * @return A new instance of a Serialiser.
	 */
	public Serialiser<T> clone();

}