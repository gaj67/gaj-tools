/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.multiserialisers;

import gaj.config.serialisers.Serialiser;

/**
 * This interface provides for management of multiple serialisers,
 * which may be applied to serialise or deserialise other objects.
 * <p/>Each serialiser is indexed via a distinct
 * type label. This type information is added to the underlying
 * serialisation strings to enable appropriate deserialisation.
 * <p/>The special type of <tt>null</tt> indicates that no
 * additional type information
 * will be added to the serialisation string,
 * and the corresponding serialiser
 * will be used to deserialise a string with no type information.
 * <p/>There are two basic ways to add a serialiser, namely via an instance
 * or via a class. If an instance is specified, then it is reused whenever
 * data of the appropriate type is serialised or deserialised.
 * If a class is specified, then it is only instantiated on first use.
 * In addition, if the serialiser class is marked with the @Singleton
 * annotation, then this instance is reused upon subsequent accesses.
 * Alternatively, if the class is not a singleton, then a new instance is
 * create and used each time.
 */
public interface MultiSerialiser extends Serialiser<Object> {

	/**
	 * Adds a new Serialiser class to the manager, indexed
	 * via the given unique type label. If the class
	 * is annotated with @Singleton, then a single instance of the
	 * serialiser will be created on first use and subsequently reused;
	 * otherwise, a new instance will be created on each use.
	 *
	 * @param typeName - The unique string label of the serialiser.
	 * @param serialiser - The class of a serialiser for the given objects.
	 * A new instance of the serialiser will be created for each use.
	 * The class must have a no-argument constructor.
	 * @param dataTypes - The optional classes of serialisable objects to
	 * be bound to the given serialiser.
	 * If this parameter is missing, then only instances of the
	 * serialiser will be serialisable.
	 * @throws InvalidArgumentException If a serialiser
	 * is already defined for the given type.
	 */
	void addSerialiser(String typeName, Class<? extends Serialiser<?>> serialiser, Class<?>... dataTypes);

	/**
	 * Adds a new Serialiser instance to the manager, indexed
	 * via the given unique type label.
	 *
	 * @param typeName - The unique string label of the serialiser.
	 * @param serialiser - An instance of a serialiser for the object.
	 * The same instance will be used in each case, so it must be
	 * stateless.
	 * @param dataTypes - The optional classes of serialisable objects to
	 * be bound to the given serialiser.
	 * If this parameter is missing, then only instances of the
	 * serialiser will be serialisable.
	 * @throws InvalidArgumentException If a serialiser
	 * is already defined for the given type.
	 */
	void addSerialiser(String typeName, Serialiser<?> serialiser, Class<?>... dataTypes);

	/**
	 * Locates or creates a Serialiser instance for the given type.
	 *
	 * @param typeName - The unique string label of the serialiser.
	 * @return An instance of the named serialiser, or a null value
	 * if the serialiser is unknown.
	 */
	/*@Nullable*/ Serialiser<?> getSerialiser(String typeName);

	/**
	 * Locates or creates a Serialiser instance for the given type.
	 *
	 * @param dataType - The class of object to be serialised
	 * or deserialised.
	 * @return An instance of the appropriate serialiser, or a null value
	 * if the serialiser is unknown.
	 */
	/*@Nullable*/ Serialiser<?> getSerialiser(Class<?> dataType);

	/**
	 * Determines the type of serialiser suitable
	 * for the given object.
	 *
	 * @param data - An instance of supposedly serialisable data.
	 * @return The unique string label of the appropriate serialiser,
	 * or a null value if no such serialiser is known.
	 */
	/*@Nullable*/ String getSerialiserType(Object data);

}