/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.keys;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/*package-private*/ class SimpleKeyTranslator implements KeyTranslator {

	/*package-private*/ SimpleKeyTranslator() {}

	@Override
	public String translateKey(String name) {
		return name;
	}

	@Override
	public String guessPropertyKey(Field field) {
		return field.getName();
	}

	@Override
	public String guessGetterKey(Method method) {
		String name = method.getName();
		if (name.startsWith("get") || name.startsWith("has"))
			name = name.substring(3);
		else if (name.startsWith("is"))
			name = name.substring(2);
		return name;
	}

	@Override
	public String guessSetterKey(Method method) {
		String name = method.getName();
		if (name.startsWith("set"))
			name = name.substring(3);
		return name;
	}

	@Override
	public String translateGlobalKey(String name) {
		return name + ".";
	}

	@Override
	public /*@Nullable*/ String guessGlobalKey(Class<?> klass) {
		return klass.getCanonicalName();
	}

}
