/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.keys;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/*package-private*/ class SimpleKeyTranslator implements KeyTranslator {

	/*package-private*/ SimpleKeyTranslator() {}

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
		String name = method.getName();
		switch (context) {
			case GETTER:
				if (name.startsWith("get") || name.startsWith("has"))
					name = name.substring(3);
				else if (name.startsWith("is"))
					name = name.substring(2);
				break;
			case SETTER:
				if (name.startsWith("set"))
					name = name.substring(3);
				break;
			default:
				throw failure("Unhandled method context: " + context);
		}
		return name;
	}

}
