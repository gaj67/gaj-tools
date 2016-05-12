/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.keys;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/*package-private*/ class VerbatimKeyTranslator implements KeyTranslator {

	/*package-private*/ VerbatimKeyTranslator() {}

	@Override
	public String getKey(String name) {
		return name;
	}

	@Override
	public String getKey(Field field) {
		return field.getName();
	}

	@Override
	public String getKey(MethodContext context, Method method) {
		return method.getName();
	}

}
