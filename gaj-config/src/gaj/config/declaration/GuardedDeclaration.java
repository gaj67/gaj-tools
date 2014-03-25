/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.declaration;

import gaj.config.annotations.Property;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Extends the BeanPropertyDeclaration by adding consistency checks.
 * Typically, values may not be altered once set.
 */
/*packge-private*/ class GuardedDeclaration extends BeanDeclaration {

	@Override
	protected void setType(Class<?> type) {
		if (this.type != null && this.type != type)
			throw new InvalidDeclarationException("Cannot override property type: " + this.type);
		this.type = type;
	}

	@Override
	protected void setKey(String key) {
		if (Property.DEFAULT_KEY == key) key = null;
		if (this.key != null && !this.key.equals(key))
			throw new InvalidDeclarationException("Cannot override key-name: " + this.key);
		this.key = key;
	}

	@Override
	protected void setRequired(boolean flag) {
		if (isRequired && !flag)
			throw new InvalidDeclarationException("Cannot override required flag");
		if (hasDefault && flag)
			throw new InvalidDeclarationException("Cannot make default property required");
		isRequired = flag;
	}

	@Override
	protected void setDefault(String value) {
		if (hasDefault &&
				(this.value == null && value != null ||
				this.value != null && !this.value.equals(value)))
			throw new InvalidDeclarationException("Cannot override default value");
		if (isRequired)
			throw new InvalidDeclarationException("Cannot make required property optional");
		hasDefault = true;
		this.value = value;
	}

	@Override
	protected void removeDefault() {
		if (hasDefault)
			throw new InvalidDeclarationException("Cannot remove default value");
	}

	@Override
	protected void setField(Field field) {
		if (this.field != null && this.field != field)
			throw new InvalidDeclarationException("Cannot override property field");
		this.field = field;
	}

	@Override
	protected void setSetter(Method method) {
		if (this.setter != null && this.setter != method)
			throw new InvalidDeclarationException("Cannot override property setter");
		setter = method;
	}

	@Override
	protected void setGetter(Method method) {
		if (this.getter != null && this.getter != method)
			throw new InvalidDeclarationException("Cannot override property getter");
		getter = method;
	}

}