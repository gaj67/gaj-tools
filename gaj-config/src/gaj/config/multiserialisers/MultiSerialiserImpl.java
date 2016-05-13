/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.multiserialisers;


import gaj.config.annotations.Annotations;
import gaj.config.serialisers.Serialiser;
import gaj.config.serialisers.Serialisers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class implements the SerialiserManager interface.
 */
/*package-private*/ class MultiSerialiserImpl implements MultiSerialiser {

	private final MultiSerialiserConfig config;
	/** The serialised representation of a null object. */
	private final String nullMarker;
	/** Maps a data-type to a serialiser type-name. */
	private final Map<Class<?>/*data-type*/, String/*type-name*/> serialiserTypes = new HashMap<>();
	/** Maps a serialiser type-name to a serialiser class. */
	private final Map<String/*type-name*/, Class<? extends Serialiser<?>>> serialiserClasses = new HashMap<>();
	/** Maps a serialiser type-name to a serialiser instance. */
	private final Map<String/*type-name*/, Serialiser<?>> serialiserInstances = new HashMap<>();

	
	/*package-private*/ MultiSerialiserImpl(MultiSerialiserConfig config, boolean useBuiltins) {
		this.config = config;
		this.nullMarker = config.getTypePrefix() + config.getNullMarker() + config.getTypeSuffix();
		
		if (useBuiltins) {
			addBuiltInSerialisers();
		}
	}

	//==========================================================================================================
	// Serialiser interface.

	@Override
	public boolean isNull(/*@Nullable*/ String data) {
		return data == null || data.equals(nullMarker);
	}

	@Override
	public /*@Nullable*/ String serialise(/*@Nullable*/ Object obj) {
		if (obj == null) return nullMarker;
		String typeName = serialiserTypes.get(obj.getClass());
		if (typeName == null)
			throw failure("Cannot obtain serialiser type for object: " + obj);
		Serialiser<?> serialiser = getSerialiser(typeName);
		if (serialiser == null)
			throw failure("Cannot obtain serialiser for type: " + typeName);
		@SuppressWarnings("unchecked")
		String data = ((Serialiser<Object>) serialiser).serialise(obj);
		return data + config.getTypePrefix() + typeName + config.getTypeSuffix();
	}

	@Override
	public /*@Nullable*/ Object deserialise(/*@Nullable*/ String data) {
		if (isNull(data)) return null;
		//Deduce type from serialisation string.
		/*@Nullable*/ String typeName = null;
		if (data.endsWith(config.getTypeSuffix())) {
			int idx = data.lastIndexOf(config.getTypePrefix());
			if (idx >= 0) {
				typeName = data.substring(idx+1, data.length()-1);
				data = data.substring(0, idx);
			}
		}
		Serialiser<?> serialiser = getSerialiser(typeName);
		if (serialiser == null)
			throw failure("Cannot deserialise type: " + typeName + " for data: " + data);
		return serialiser.deserialise(data);
	}

	//==========================================================================================================
	// MultiSerialiser interface.

	@Override
	public /*@Nullable*/ Serialiser<?> getSerialiser(/*@Nullable*/ String typeName) {
		Serialiser<?> serialiser = serialiserInstances.get(typeName);
		if (serialiser != null) return serialiser;
		Class<? extends Serialiser<?>> klass = serialiserClasses.get(typeName);
		if (klass == null) return null;
		try {
			serialiser = klass.newInstance();
			if (Annotations.isSingleton(klass)) serialiserInstances.put(typeName, serialiser);
			return serialiser;
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
	}

	@Override
	public /*@Nullable*/ Serialiser<?> getSerialiser(Class<?> dataType) {
		String typeName = serialiserTypes.get(dataType);
		return (typeName == null) ? null : getSerialiser(typeName);
	}

	@Override
	public void addSerialiser(String typeName, Class<? extends Serialiser<?>> serialiser, Class<?>... dataTypes) {
		if (serialiserClasses.get(typeName) != null || serialiserInstances.get(typeName) != null)
			throw new IllegalArgumentException("Cannot override existing serialiser of type: " + typeName);
		_addSerialiser(typeName, serialiser, dataTypes);
	}

	@Override
	public void addSerialiser(/*@Nullable*/ String typeName, Serialiser<?> serialiser, Class<?>... dataTypes) {
		if (serialiserClasses.get(typeName) != null || serialiserInstances.get(typeName) != null)
			throw new IllegalArgumentException("Cannot override existing serialiser of type: " + typeName);
		_addSerialiser(typeName, serialiser, dataTypes);
	}

	@Override
	public /*@Nullable*/ String getSerialiserType(/*@Nullable*/ Object data) {
		return (data == null) ? null : serialiserTypes.get(data.getClass());
	}

	//==========================================================================================================
	// Implementation.

	private void addBuiltInSerialisers() {
		for (Entry<Class<?>, Class<? extends Serialiser<?>>> entry : Serialisers.getSerialiserClasses().entrySet()) {
			Class<?> type = entry.getKey();
			String key = type.getSimpleName().toLowerCase();
			_addSerialiser(key, entry.getValue(), type);
		}			
		Class<? extends Serialiser<?>> stringSerialiser = serialiserClasses.get("string");
		if (stringSerialiser != null) {
			_addSerialiser(null, stringSerialiser);
			// Now override string default to str.
			_addSerialiser("str", stringSerialiser, String.class);
		}
		@SuppressWarnings("unchecked")
		Class<? extends Serialiser<?>> intSerialiser = serialiserClasses.get("integer");
		if (intSerialiser != null) {
			// Now override integer default to int.
			_addSerialiser("int", intSerialiser, int.class, Integer.class);
		}
		@SuppressWarnings("unchecked")
		Class<? extends Serialiser<?>> boolSerialiser = serialiserClasses.get("boolean");
		if (boolSerialiser != null) {
			// Now override boolean default to bool.
			_addSerialiser("bool", boolSerialiser, boolean.class, Boolean.class);
		}
	}

	private void _addSerialiser(String typeName, Class<? extends Serialiser<?>> serialiser, Class<?>... dataTypes) {
		serialiserClasses.put(typeName, serialiser);
		if (dataTypes.length != 0) {
			for (Class<?> object : dataTypes)
				serialiserTypes.put(object, typeName);
		}
	}

	private void _addSerialiser(String typeName, Serialiser<?> serialiser, Class<?>... dataTypes) {
		serialiserInstances.put(typeName, serialiser);
		if (dataTypes.length != 0) {
			for (Class<?> object : dataTypes)
				serialiserTypes.put(object, typeName);
		}
	}

}