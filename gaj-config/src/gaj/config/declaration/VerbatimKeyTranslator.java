/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.declaration;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/*package-private*/ class VerbatimKeyTranslator implements KeyTranslator {

	@Override
	public String getKey(String name) {
		return name;
	}

	@Override
	public String getKey(Field field) {
		return field.getName();
	}

	@Override
	public String getKey(Context context, Method method) {
		return method.getName();
	}

}
