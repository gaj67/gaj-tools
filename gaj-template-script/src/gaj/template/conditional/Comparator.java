/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.conditional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Used to test conditional variables.
 * <pre>
 *<em>CONDITIONAL</em> := <tt><em>variable</em></tt> [ <em>UNARY_COMPARATOR</em> | <em>BINARY_COMPARATOR</em> <tt><em>value</em></tt> ]
 *
 *<em>UNARY_COMPARATOR</em> := <em>IS_DEFINED</em> | <em>NOT_DEFINED</em> | <em>IS_TRUE</em> | <em>IS_FALSE</em>
 *
 *<em>BINARY_COMPARATOR</em> := <em>NOT_EQUAL</em> | <em>LESS_OR_EQUAL</em> | <em>MORE_OR_EQUAL</em> |
 *                     <em>IS_EQUAL</em> | <em>LESS_THAN</em> | <em>MORE_THAN</em> | <em>MATCHES_PATTERN</em>
 *
 *<em>IS_DEFINED</em> := ""; <em>NOT_DEFINED</em> := "!"; <em>IS_TRUE</em> := "+"; <em>IS_FALSE</em> := "-";
 *<em>NOT_EQUAL</em> := "!="; <em>LESS_OR_EQUAL</em> := "<="; <em>MORE_OR_EQUAL</em> := ">=";
 *<em>IS_EQUAL</em> := "="; <em>LESS_THAN</em> := "<"; <em>MORE_THAN</em> := ">"
 *<em>MATCHES_PATTERN</em> := "~="
 * </pre>
 */
/*package-private*/ enum Comparator {
    // XXX: The order is important for search, e.g. must detect "<=" before "<", etc.

    /**
     * Used to test for a variable with a defined value (i.e. not null and not
     * having an empty or blank string representation). <br/>Example
     * conditional: "X".
     */
    IS_DEFINED("", true),
    /**
     * Used to test for a variable with an undefined value (i.e. null or having
     * an empty or blank string representation). <br/>Example conditional: "X!".
     */
    NOT_DEFINED("!", true),
    /**
     * Used to test for a variable with a value that evaluates to true (i.e.
     * matching one of "1", "Y", "T or "true", case insensitively). <br/>Example
     * conditional: "X+".
     */
    IS_TRUE("+", true),
    /**
     * Used to test for a variable with a value that evaluates to false (i.e.
     * matching one of "0", "N", "F" or "false", case insensitively).
     * <br/>Example conditional: "X-".
     */
    IS_FALSE("-", true),
    /**
     * Used to test for a variable with a value that matches the given pattern.
     * <br/>Note: Currently, only simple patterns can be used, which contain no
     * parentheses. <br/>Example conditionals: <dl><dt>X~=^A</dt><dd>Value of X
     * starts with 'A'.</dd> <dt>X~=^[^A]</dt><dd>Value of X starts with a
     * character other than 'A'.</dd> <dt>X~=C$</dt><dd>Value of X ends with
     * 'C'.</dd> <dt>X~=b</dt><dd>Value of X contains 'b'.</dd>
     * <dt>X~=[^b]</dt><dd>Value of X contains at least one character that is
     * not 'b' (but may also contain 'b').</dd> </dl>
     */
    MATCHES_PATTERN("~="),
    /**
     * Used to test for a variable with a value that does not match the given
     * value. <br/>Example conditional: "X!=Y".
     */
    NOT_EQUAL("!="),
    /**
     * Used to test for a variable with a value that is less than or equal to
     * the given value. <br/>Example conditional: "X<=3".
     */
    LESS_OR_EQUAL("<=", false),
    /**
     * Used to test for a variable with a value that is greater than or equal to
     * the given value. <br/>Example conditional: "X>=3".
     */
    MORE_OR_EQUAL(">=", false),
    /**
     * Used to test for a variable with a value that matches the given value.
     * <br/>Example conditional: "X=Y".
     */
    IS_EQUAL("=", false),
    /**
     * Used to test for a variable with a value that is less than the given
     * value. <br/>Example conditional: "X<3".
     */
    LESS_THAN("<"),
    /**
     * Used to test for a variable with a value that is greater than the given
     * value. <br/>Example conditional: "X>3".
     */
    MORE_THAN(">");
    public final String value;
    public final boolean isBinary;
    public final boolean hasEquals;
    private static final List<Comparator> _BINARY_COMPARATORS = new ArrayList<Comparator>();

    static {
        for (Comparator c : values()) {
            if (c.isBinary) {
                _BINARY_COMPARATORS.add(c);
            }
        }
    }
    public static final List<Comparator> BINARY_COMPARATORS = Collections.unmodifiableList(_BINARY_COMPARATORS);
    private static final List<Comparator> _UNARY_COMPARATORS = new ArrayList<Comparator>();

    static {
        for (Comparator c : values()) {
            if (!c.isBinary) {
                _UNARY_COMPARATORS.add(c);
            }
        }
    }
    public static final List<Comparator> UNARY_COMPARATORS = Collections.unmodifiableList(_UNARY_COMPARATORS);
    private static final List<Comparator> _SEARCHABLE_UNARY_COMPARATORS = new ArrayList<Comparator>();

    static {
        for (Comparator c : values()) {
            if (!c.isBinary && !c.value.isEmpty()) {
                _SEARCHABLE_UNARY_COMPARATORS.add(c);
            }
        }
    }

    private Comparator(String value, boolean isUnaryElseHasEquals) {
        this.value = value;
        this.isBinary = this.hasEquals = !isUnaryElseHasEquals;
    }

    private Comparator(String value) {
        this.value = value;
        this.isBinary = true;
        this.hasEquals = false;
    }

    /**
     * Splits a conditional statement, of the form:
     * <pre><tt><em>variable</em></tt> [<tt><em>unary-comparator</em></tt> | <tt><em>binary-comparator</em></tt> <tt><em>value</em></tt>]]</pre>
     * into an Object array of constituent parts, in the form:
     * <pre>[ <tt><em>variable</em></tt>, <tt><em>comparator</em></tt>, <tt><em>value</em></tt> ]</pre>
     * where the comparator element is the enumerated type, not the string
     * value. If a unary comparator is used, then the value element will be
     * null.
     *
     * @param conditional - The conditional statement.
     * @return An 3-array of conditional constituents.
     */
    /*package-private*/ static Object[] parse(String conditional) {
        if (conditional == null || conditional.trim().isEmpty()) {
            throw new ConditionalParseException("No variable found");
        }
        // Search for binary comparator first.
        for (Comparator comparator : BINARY_COMPARATORS) {
            int idx = conditional.indexOf(comparator.value);
            if (idx == 0)
                throw new ConditionalParseException("Must have a variable for comparison");
            if (idx > 0)
                return new Object[]{conditional.substring(0, idx), comparator, conditional.substring(idx + comparator.value.length())};
        }
        // Now search for unary comparator.
        final int len = conditional.length();
        for (Comparator comparator : _SEARCHABLE_UNARY_COMPARATORS) {
            int idx = conditional.indexOf(comparator.value);
            if (idx >= 0) {
                if (idx == 0 // Must have a variable for comparison!
                        || idx + comparator.value.length() != len) // Unary comparator must come after variable, with nothing following.
                {
                    throw new ConditionalParseException("Invalid comparison");
                }
                return new Object[]{conditional.substring(0, idx), comparator, null};
            }
        }
        return new Object[]{conditional, Comparator.IS_DEFINED, null}; // Default if no comparator found.
    }

    /**
     * Determines whether or not a property value is classed as being 'defined',
     * which means it is non-null, and its string representation is non-empty
     * and not blank (i.e. containing only whitespace).
     *
     * @param value - The object representing the property value.
     * @return A value of true (or false) if the object does (or does not) have
     * a defined textual representation.
     */
    public static boolean isDefined(Object value) {
        return value != null && !value.toString().trim().isEmpty();
    }

    /**
     * Determines whether or not a property value is classed as being
     * 'undefined', which means it is either null or its string representation
     * is empty or blank (i.e. containing only whitespace).
     *
     * @param value - The object representing the property value.
     * @return A value of true (or false) if the object does not (or does) have
     * a defined textual representation.
     */
    public static boolean isUndefined(Object value) {
        return value == null || value.toString().trim().isEmpty();
    }

    /**
     * Determines whether or not a property value evaluates to 'true', which
     * means it is non-null and its string representation matches (case
     * insensitively) one of: "1"; "Y"; "T"; "true".
     * <p/>
     * Note that if isTrue() returns false, this is no guarantee that isFalse()
     * will return true.
     *
     * @param value - The object representing the property value.
     * @return A value of true (or false) if the object does (or does not)
     * evaluate to true.
     */
    public static boolean isTrue(Object value) {
        String vstr;
        return value != null
                && ((vstr = value.toString()).equals("1") || vstr.equalsIgnoreCase("Y")
                || vstr.equalsIgnoreCase("T") || vstr.equalsIgnoreCase("true"));
    }

    /**
     * Determines whether or not a property value evaluates to 'false', which
     * means it is non-null and its string representation matches (case
     * insensitively) one of: "0"; "N"; "F"; "false".
     * <p/>
     * Note that if isFalse() returns false, this is no guarantee that isTrue()
     * will return true.
     *
     * @param value - The object representing the property value.
     * @return A value of true (or false) if the object does (or does not)
     * evaluate to true.
     */
    public static boolean isFalse(Object value) {
        String vstr;
        return value != null
                && ((vstr = value.toString()).equals("0") || vstr.equalsIgnoreCase("N")
                || vstr.equalsIgnoreCase("F") || vstr.equalsIgnoreCase("false"));
    }

    /**
     * Determines whether the given value evaluates to true or false under the
     * given unary comparator.
     *
     * @param value - The (possibly null) value to test.
     * @return A value of true if the IS_TRUE comparator evaluates the given
     * value to true or the IS_FALSE comparator evaluates the given value to
     * false, or a value of false otherwise.
     */
    public boolean evaluate(Object value) {
        switch (this) {
            case NOT_DEFINED:
                return isUndefined(value);
            case IS_DEFINED:
                return isDefined(value);
            case IS_FALSE:
                return isFalse(value);
            case IS_TRUE:
                return isTrue(value);
            default:
                throw new ConditionalParseException("Unhandled comparator: " + this);
        }
    }

    /**
     * Determines whether the two given date values agree under the binary
     * comparator.
     *
     * @param date - The (possibly null) date value to test.
     * @param comparisonDate - The (possibly null) date value to be tested
     * against.
     * @return A value of true (or false) if the two values do (or do not) agree
     * under the comparator.
     */
    public boolean evaluate(Date date, Date comparisonDate) {
        if (date == null) {
            return (comparisonDate == null && this.hasEquals || comparisonDate != null && this == Comparator.NOT_EQUAL);
        } else if (comparisonDate == null) {
            return (this == Comparator.NOT_EQUAL);
        }
        return compare(date.compareTo(comparisonDate));
    }

    private boolean compare(int cmp) {
        switch (this) {
            case IS_EQUAL:
                return cmp == 0;
            case LESS_OR_EQUAL:
                return cmp <= 0;
            case LESS_THAN:
                return cmp < 0;
            case MORE_OR_EQUAL:
                return cmp >= 0;
            case MORE_THAN:
                return cmp > 0;
            case NOT_EQUAL:
                return cmp != 0;
            default:
                throw new ConditionalParseException("Unhandled comparator: " + this);
        }
    }

    /**
     * Determines whether the two given integer values agree under the binary
     * comparator.
     *
     * @param value - The (possibly null) integer value to test.
     * @param comparisonValue - The (possibly null) integer value to be tested
     * against.
     * @return A value of true (or false) if the two values do (or do not) agree
     * under the comparator.
     */
    public boolean evaluate(Integer value, Integer comparisonValue) {
        if (value == null) {
            return (comparisonValue == null && this.hasEquals || comparisonValue != null && this == Comparator.NOT_EQUAL);
        } else if (comparisonValue == null) {
            return (this == Comparator.NOT_EQUAL);
        }
        return compare((value < comparisonValue) ? -1 : ((value > comparisonValue) ? 1 : 0));
    }

    /**
     * Determines whether the two given double values agree under the binary
     * comparator.
     *
     * @param value - The (possibly null) double value to test.
     * @param comparisonValue - The (possibly null) double value to be tested
     * against.
     * @return A value of true (or false) if the two values do (or do not) agree
     * under the comparator.
     */
    public boolean evaluate(Double value, Double comparisonValue) {
        if (value == null) {
            return (comparisonValue == null && this.hasEquals || comparisonValue != null && this == Comparator.NOT_EQUAL);
        } else if (comparisonValue == null) {
            return (this == Comparator.NOT_EQUAL);
        }
        return compare((value < comparisonValue) ? -1 : ((value > comparisonValue) ? 1 : 0));
    }

    /**
     * Determines whether the textual representations of the two given values
     * agree under the binary comparator.
     * <p/>
     * Note: A null value is regarded here as being equivalent to an empty
     * string.
     * <p/>
     * Note: The textual representations are trimmed of leading and trailing
     * whitespace prior to the comparison.
     *
     * @param value - The (possibly null) property value to test.
     * @param comparisonValue - The (possibly null) value to be tested against.
     * @return A value of true (or false) if the two values do (or do not) agree
     * under the comparator.
     */
    public boolean evaluate(Object value, Object comparisonValue) {
        String vstr = (value == null) ? "" : value.toString().trim();
        String cstr = (comparisonValue == null) ? "" : comparisonValue.toString().trim();
        return compare(vstr.compareTo(cstr));
    }

    /**
     * Determines whether the textual representation of the given value matches
     * the given pattern string.
     * <p/>
     * Note: A null value is regarded here as being equivalent to an empty
     * string.
     * <p/>
     * Note: The textual representations are trimmed of leading and trailing
     * whitespace prior to the comparison.
     * <p/>
     * Note: The pattern will be extended with a leading ^.*?" or a trailing
     * ".*?$" as necessary, to ensure the entire value string is matched.
     *
     * @param value - The (possibly null) property value to test.
     * @param pattern - The pattern to be tested against.
     * @return A value of true (or false) if the given value does (or does not)
     * match the given pattern.
     */
    static public boolean match(Object value, String pattern) {
        String pstr = (pattern == null) ? "" : pattern.trim();
        if (!pstr.startsWith("^")) {
            pstr = "^.*?" + pstr;
        }
        if (!pstr.endsWith("$")) {
            pstr += ".*?$";
        }
        return Pattern.matches(pstr, (value == null) ? "" : value.toString().trim());
    }
}