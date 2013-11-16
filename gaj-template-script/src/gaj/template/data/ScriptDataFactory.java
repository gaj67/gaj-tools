/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class ScriptDataFactory {

    private ScriptDataFactory() {}

    /**
     * Creates an empty script data object.
     * 
     * @return The script data instance.
     */
    public static ScriptData newData() {
        return new ScriptDataImpl();
    }

    /**
     * Creates a script data object populated with the given properties.
     * 
     * @return The script data instance.
     */
    public static ScriptData newData(Object... properties) {
        ScriptData data = new ScriptDataImpl();
        if (properties.length % 2 != 0) {
            throw newException("Broken list of key/value pairs - must be an even number of elements");
        }
        for (int i = 0; i < properties.length; i++) {
            if (!(properties[i] instanceof String)) {
                throw newException("Invalid non-string key: " + properties[i]);
            }
            data.setProperty((String) properties[i], properties[++i]);
        }
        return data;
    }

    public static ScriptData backoffData(ScriptData... backoff) {
        return new BackoffScriptDataImpl(backoff);
    }

    public static ScriptDataException newException(String message) {
        return new ScriptDataException(message);
    }

    /**
     * Attempts to convert the given property value to an integer.
     *
     * @param value - The property value.
     * @return The integer value of the property, or a value of Integer.MIN_VAUE if the
     * property value cannot be converted to an integer.
     */
    public static int toInt(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).intValue();
        } else if (value != null) {
            try {
                return Integer.valueOf(value.toString());
            } catch (NumberFormatException e) {
                // Ignore and fall through.
            }
        }
        return Integer.MIN_VALUE;
    }

    /**
     * Attempts to convert the given property value to an Integer.
     *
     * @param value - The property value.
     * @return The integer value of the property, or a null value if the
     * property value cannot be converted to an integer.
     */
    public static Integer toInteger(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Integer) {
            return (Integer) value;
        }
        try {
            return Integer.valueOf(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Attempts to convert the given property value to a double.
     *
     * @param value - The property value.
     * @return The double value of the property, or a null value if the property
     * value cannot be converted to a double.
     */
    public static Double toDouble(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Double) {
            return (Double) value;
        }
        try {
            return Double.valueOf(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Attempts to convert the given property value to a date.
     *
     * @param value - The property value.
     * @return The date value of the property, or a null value if the property
     * value cannot be converted to a date.
     */
    public static Date toDate(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Date) {
            return (Date) value;
        } else {
            return parseDate(value.toString());
        }
    }

    private static Date parseDate(String date) {
        try {
            return DateFormat.getDateTimeInstance().parse(date);
        } catch (ParseException e) {
            try {
                return DateFormat.getDateInstance().parse(date);
            } catch (ParseException e2) {
                return null;
            }
        }
    }

    /**
     * Attempts to convert the given property value to the specified type.
     *
     * @param value - The property value.
     * @return The value of the property as the specified type, or a null value
     * if the property cannot be converted to that type.
     */
    @SuppressWarnings("unchecked")
    public static <T> T toType(Object value, Class<T> type) {
        if (value == null) {
            return null;
        } else if (type == Integer.class) {
            return (T) toInteger(value);
        } else if (type == Double.class) {
            return (T) toDouble(value);
        } else if (type == Date.class) {
            return (T) toDate(value);
        } else if (type.isAssignableFrom(value.getClass())) {
            return (T) value;
        } else {
            return null;
        }
    }

}
