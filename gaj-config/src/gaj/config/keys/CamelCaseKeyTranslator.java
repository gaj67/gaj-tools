/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.config.keys;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/*package-private*/ class CamelCaseKeyTranslator implements KeyTranslator {

	private final String separator;

	/**
	 * Creates an instance of the 
	 * separated key-name translator, using
	 * the given separator.
	 * 
	 * @param sep - The separator string.
	 */
	CamelCaseKeyTranslator(String sep) {
		this.separator = sep;
	}

	@Override
	public String translateKey(String name) {
		return name;
	}

	@Override
	public String guessPropertyKey(Field field) {
		return join(decomposeCamelCase(field.getName()));
	}

	@Override
	public String guessGetterKey(Method method) {
		List<String> parts = decomposeCamelCase(method.getName());
		String lead = parts.get(0);
		if (lead.equalsIgnoreCase("get") ||	lead.equalsIgnoreCase("is") || lead.equalsIgnoreCase("has"))
			parts.remove(0);
		return join(parts);
	}

	@Override
	public String guessSetterKey(Method method) {
		List<String> parts = decomposeCamelCase(method.getName());
		String lead = parts.get(0);
		if (lead.equalsIgnoreCase("set"))
			parts.remove(0);
		return join(parts);
	}

	private static List<String> decomposeCamelCase(String s) {
		List<String> list = new LinkedList<String>();
		final int n = s.length();
		int start = 0, end = 0;
		while (start < n) {
			// Handle lower case.
			while (end < n && Character.isLowerCase(s.charAt(end))) end++;
			if (end > start) {
				// E.g. "var" in "var" or "varPath", or "Var" in "getVar" or "getVarPath".
				list.add(s.substring(start, end));
				if (end >= n) break;
				start = end;
			}
			// Handle upper case.
			end++;
			while (end < n && Character.isUpperCase(s.charAt(end))) end++;
			if (end >= n) {
				// E.g. "URL" in "URL" or "getURL".
				list.add(s.substring(start, end));
				break;
			} else if (end > start + 1) {
				// E.g. "URL[P]" in "getURLPath".
				list.add(s.substring(start, end-1));
				start = end - 1;
			} /* else {
		        // E.g. "V" in "getVar".
		        // Fall through to lower case processing.
		    } */
		}
		return list;
	}

	private String join(List<String> list) {
		StringBuilder buf = new StringBuilder();
		for (String name : list) {
			if (buf.length() > 0)
				buf.append(separator);
			buf.append(name.toLowerCase());
		}
		return buf.toString();
	}

	@Override
	public String translateGlobalKey(String name) {
		return name + separator;
	}

	@Override
	public /*@Nullable*/ String guessGlobalKey(Class<?> klass) {
		String name = klass.getCanonicalName();
		return (name != null && !".".equals(separator)) ? name.replace(".", separator) : name; 
	}

}
