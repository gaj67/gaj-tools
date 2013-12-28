/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.serial;


import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the SerialiserManager interface.
 */
/*package-private*/ class SerialiserManagerImpl extends ConfigurableSerialiser<Object> implements SerialiserManager {

	/** Maps serialiser identifier to serialiser instance or class. */
	private Map<String,Object> deserialisers = new HashMap<String,Object>();
	/** Maps object class to serialiser identifier. */
	private Map<Class<?>,String> serialisers = new HashMap<Class<?>,String>();

	/*package-private*/ SerialiserManagerImpl(SerialiserConfig config, boolean useBuiltins) {
		super(config);
		if (useBuiltins) {
			_addSerialiser("integer", new IntegerSerialiser(config), Integer.class);
			// Now override integer default to int.
			_addSerialiser("int", new IntegerSerialiser(config), int.class, Integer.class);
			_addSerialiser("short", new ShortSerialiser(config), short.class, Short.class);
			_addSerialiser("long", new LongSerialiser(config), long.class, Long.class);
			_addSerialiser("double", new DoubleSerialiser(config), double.class, Double.class);
			_addSerialiser("float", new FloatSerialiser(config), float.class, Float.class);
			_addSerialiser("boolean", new BooleanSerialiser(config), boolean.class, Boolean.class);
			// Now override boolean default to bool.
			_addSerialiser("bool", new BooleanSerialiser(config), boolean.class, Boolean.class);
			_addSerialiser("string", new StringSerialiser(config), String.class);
			// Now override string default to str.
			_addSerialiser("str", new StringSerialiser(config), String.class);
			_addSerialiser(null, new StringSerialiser(config), String.class);
		}
	}

	private void _addSerialiser(String type, Serialiser<?> serialiser, Class<?>... objects) {
		deserialisers.put(type, serialiser);
		if (objects.length == 0) {
			// Self-serialiser.
			serialisers.put(serialiser.getClass(), type);
		} else {
			// External serialiser.
			for (Class<?> object : objects)
				serialisers.put(object, type);
		}
	}

	private void _addSerialiser(String type, Class<? extends Serialiser<?>> serialiser, Class<?>... objects) {
		deserialisers.put(type, serialiser);
		if (objects.length == 0) {
			// Self-serialiser.
			serialisers.put(serialiser, type);
		} else {
			// External serialiser.
			for (Class<?> object : objects)
				serialisers.put(object, type);
		}
	}

	@Override
	public void addSerialiser(String type, Serialiser<?> serialiser, Class<?>... objects)
			throws InvalidSerialisationException {
		if (deserialisers.get(type) != null)
			throw new InvalidSerialisationException(
					"Cannot override existing serialiser otype: " + type
					);
		_addSerialiser(type, serialiser, objects);
	}

	@Override
	public void addSerialiser(String type,
			Class<? extends Serialiser<?>> serialiser, Class<?>... objects)
					throws InvalidSerialisationException {
		if (deserialisers.get(type) != null)
			throw new InvalidSerialisationException(
					"Cannot override existing serialiser otype: " + type
					);
		_addSerialiser(type, serialiser, objects);
	}

	@Override
	public /*@Nullable*/ Serialiser<?> getSerialiser(/*@Nullable*/ String type) {
		Object obj = deserialisers.get(type);
		if (obj == null) return null;
		if (obj instanceof Serialiser)
			return (Serialiser<?>)obj;
		try {
			@SuppressWarnings("unchecked")
			Serialiser<?> serialiser = ((Class<? extends Serialiser<?>>)obj).newInstance();
			if (serialiser instanceof ConfigurableSerialiser<?>)
				((ConfigurableSerialiser<?>)serialiser).configure(config);
			//TODO: if (isSingleton(obj)) deserialisers.put(type, serialiser);
			return serialiser;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new InvalidSerialisationException(e);
		}
	}

	@Override
	public /*@Nullable*/ Serialiser<?> getSerialiser(Class<?> dataClass) {
		return getSerialiser(serialisers.get(dataClass));
	}

	@Override
	public String getSerialiserType(/*@Nullable*/ Object obj) {
		return (obj == null) ? null : serialisers.get(obj.getClass());
	}

	@Override
	public String serialise(/*@Nullable*/ Object obj)
			throws InvalidSerialisationException {
		if (obj == null) return config.getNullMarker();
		/*@Nullale*/ String type = serialisers.get(obj.getClass());
		/*@Nullale*/ Serialiser<?> serialiser = getSerialiser(type);
		if (serialiser == null)
			throw new InvalidSerialisationException("Cannot find serialiser for object: " + obj);
		@SuppressWarnings("unchecked")
		String data = ((Serialiser<Object>) serialiser).serialise(obj);
		return (type == null || serialiser == deserialisers.get(null))
				? data // XXX Special behaviour: Don't add type information.
						: (data + config.getTypePrefix() + type + config.getTypeSuffix());
	}

	@Override
	public /*@Nullable*/ Object deserialise(/*@Nullable*/ String data) throws InvalidSerialisationException {
		if (isNull(data)) return null;
		//Deduce type from serialisation string.
		/*@Nullable*/ String type = null;
		if (data.endsWith(config.getTypeSuffix())) {
			int idx = data.lastIndexOf(config.getTypePrefix());
			if (idx >= 0) {
				type = data.substring(idx+1, data.length()-1);
				data = data.substring(0, idx);
			}
		}
		Serialiser<?> serialiser = getSerialiser(type);
		if (serialiser == null)
			throw new InvalidSerialisationException("Cannot deserialise type: " + type + " for data: " + data);
		return serialiser.deserialise(data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SerialiserManager clone() {
		SerialiserManagerImpl obj = (SerialiserManagerImpl) super.clone();
		obj.serialisers = (Map<Class<?>,String>)((HashMap<Class<?>,String>)serialisers).clone();
		obj.deserialisers = (Map<String,Object>)((HashMap<String,Object>)deserialisers).clone();
		return obj;
	}

}