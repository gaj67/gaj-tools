/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.declaration;

import gaj.config.annotations.Property;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A simple container of information with getters and setters.
 * No checks for consistency or legality are made.
 */
/*package-private*/ class BeanDeclaration implements Declaration {

	protected Class<?> type = null;
	protected String key = null;
	protected boolean isRequired = false;
	protected boolean hasDefault = false;
	protected String value = null;
	protected Field field = null;
	protected Method getter = null;
	protected Method setter = null;

	protected BeanDeclaration() {}

	@Override
	public Class<?> getDataType() {
		return type;
	}

	protected void setType(Class<?> type) {
		this.type = type;
	}

	@Override
	public String getKey() {
		return key;
	}

	/**
	 * Sets the configuration key-name string associated with the property.
	 * @param key - The key-name to set.
	 */
	protected void setKey(String key) {
		this.key = Property.DEFAULT_KEY.equals(key) ? null : key;
	}

	@Override
	public boolean isRequired() {
		return isRequired;
	}

	/**
	 * Sets the required status of the property to the given value.
	 * @param flag - The boolean value to set.
	 */
	protected void setRequired(boolean flag) {
		isRequired = flag;
	}

	@Override
	public boolean hasDefault() {
		return hasDefault;
	}

	@Override
	public /*@Nullable*/ String getValue() {
		return hasDefault ? value : null;
	}

	/**
	 * Sets the default value for the property,
	 * serialised as a string.
	 * A subsequent call to hasDefault() will return true.
	 * @param value - The default value.
	 */
	protected void setDefault(String value) {
		hasDefault = true;
		this.value = value;
	}

	/**
	 * Removes the default value, if any, for the property.
	 * A subsequent call to hasDefault() will return false.
	 */
	protected void removeDefault() {
		hasDefault = false;
		value = null;
	}

	@Override
	public boolean isSettable() {
		return setter != null || field != null;
	}

	@Override
	public boolean isGettable() {
		return getter != null || field != null;
	}

	@Override
	public Field getField() {
		return field;
	}

	/**
	 * Sets a field to be associated with the property.
	 * @param field - The field to set.
	 */
	protected void setField(Field field) {
		this.field = field;
	}

	@Override
	public Method getSetter() {
		return setter;
	}

	/**
	 * Sets a setter method for the property.
	 * @param method - The method to set.
	 */
	protected void setSetter(Method method) {
		setter = method;
	}

	@Override
	public Method getGetter() {
		return getter;
	}

	/**
	 * Sets a getter method for the property.
	 * @param method - The method to set.
	 */
	protected void setGetter(Method method) {
		getter = method;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("{");
		if (key != null)
			buf.append("key="+key);
		if (type != null) {
			if (buf.length() > 1) buf.append(",");
			buf.append("type="+type);
		}
		if (hasDefault) {
			if (buf.length() > 1) buf.append(",");
			buf.append("default="+value);
		}
		if (isRequired) {
			if (buf.length() > 1) buf.append(",");
			buf.append("required=true");
		}
		if (field != null) {
			if (buf.length() > 1) buf.append(",");
			buf.append("field="+field);
		}
		if (getter != null) {
			if (buf.length() > 1) buf.append(",");
			buf.append("getter="+getter);
		}
		if (setter != null) {
			if (buf.length() > 1) buf.append(",");
			buf.append("setter="+setter);
		}
		buf.append("}");
		return buf.toString();
	}

	/**
	 * Merges one or more property declarations
	 * into the current declaration.
	 * 
	 * @param declarations - The declarations to be merged.
	 */
	public void merge(Declaration... declarations) {
		for (Declaration dec : declarations) {
			setType(dec.getDataType());
			String key = dec.getKey();
			if (key != null) setKey(key);
			if (dec.hasDefault()) setDefault(dec.getValue());
			if (dec.isRequired()) setRequired(true);
			Field field = dec.getField();
			if (field != null) setField(field);
			Method getter = dec.getGetter();
			if (getter != null) setGetter(getter);
			Method setter = dec.getSetter();
			if (setter != null) setSetter(setter);
		}
	}

}