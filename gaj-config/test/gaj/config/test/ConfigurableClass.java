package gaj.config.test;

import gaj.config.annotations.Configurable;
import gaj.config.annotations.Default;
import gaj.config.annotations.Getter;
import gaj.config.annotations.Property;
import gaj.config.annotations.Required;
import gaj.config.annotations.Setter;
import gaj.config.annotations.Singleton;

@Singleton
@Configurable("fred")
public class ConfigurableClass {

	public double nonPropertyField = 0;

	@Required
	public int intField = (int) nonPropertyField;

	@Property("abc.value")
	public String stringField;

	@Default("null")
	public Object objField;

	public int getInt() {
		return 0;
	}

	@Getter
	public String getABCValue() {
		return stringField;
	}

	@Setter("int.field")
	public void setter(int value) {
		intField = value + getInt();
	}

}
