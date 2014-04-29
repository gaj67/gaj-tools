/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.conditional;

import gaj.template.data.ScriptData;
import gaj.template.data.ScriptDataFactory;
import java.util.Date;

/**
 * Used to map a conditional string in an object that can be used for evaluating
 * an embedding data property.
 * <pre><em>CONDITIONAL</em> := [ "[" <em>TYPE</em> "]" ] <tt><em>variable</em></tt> [ <em>UNARY_COMPARATOR</em> | <em>BINARY_COMPARATOR</em> <tt><em>value</em></tt> ]</pre>
 * Note: For the <em>IS_EQUAL</em> and <em>NOT_EQUAL</em> comparators, the value
 * may optionally contain multiple choices, in the form:
 * <pre><em>MULTI_VALUE</em> := <tt><em>value</em></tt> { "|" <tt><em>value</em></tt> }</pre>
 *
 * @see Type {@link Type} &nbsp;- for more information about type conversion.
 * @see Comparator {@link Comparator} &nbsp;- for more information about unary
 * and binary comparators.
 */
/*package-private*/ class Conditional implements Evaluator {

    private final Type type;
    private final String variable;
    private final Comparator comparator;
    private final String value;
    private final String[] values;
    private final Boolean junctionFlag;

    /*package-private*/ Conditional(Type type, String variable, Comparator comparator, String value, String[] values, Boolean junctionFlag) {
        this.type = type;
        this.variable = variable;
        this.comparator = comparator;
        this.value = value;
        this.values = values;
        this.junctionFlag = junctionFlag;
    }

    /**
     * Determines whether the conditional evaluates to true or false for the
     * given property value.
     *
     * @param propertyValue - The value of the property named by the conditional
     * variable. This may be null.
     * @return A value of true (or false) if the conditional does (or does not)
     * evaluate to true for the given property value.
     */
    @Override
    public boolean evaluate(ScriptData data) {
        Object propertyValue = data.getProperty(variable);
        if (type != null) {
            propertyValue = ScriptDataFactory.toType(data, type.type);
        }
        if (value == null) {
            // Base decision only on property value, using unary comparator.
            return comparator.evaluate(propertyValue);
        } else {
            // Check property value matches given value(s), using binary comparator.
            if (junctionFlag == null) {
                // Neither disjunction nor conjunction - evaluate single value.
                return evaluate(propertyValue, value);
            } else if (junctionFlag == true) {
                // Special disjunctive case: allow form X=A|B, meaning X can take value A or B.
                for (String comparisonValue : values) {
                    if (evaluate(propertyValue, comparisonValue)) {
                        return true;
                    }
                }
                return false;
            } else /*if (junctionFlag == false)*/ {
                // Special conjunctive case: allow form X!=A|B, meaning X can take neither value A nor B.
                for (String comparisonValue : values) {
                    if (!evaluate(propertyValue, comparisonValue)) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    private boolean evaluate(Object propertyValue, String comparisonValue) {
        if (comparator == Comparator.MATCHES_PATTERN) {
            return Comparator.match(propertyValue, comparisonValue);
        } else if (propertyValue instanceof Date) {
            return comparator.evaluate((Date) propertyValue, ScriptDataFactory.toDate(comparisonValue));
        } else if (propertyValue instanceof Integer) {
            return comparator.evaluate((Integer) propertyValue, ScriptDataFactory.toInteger(comparisonValue));
        } else if (propertyValue instanceof Double) {
            return comparator.evaluate((Double) propertyValue, ScriptDataFactory.toDouble(comparisonValue));
        } else {
            return comparator.evaluate(propertyValue, comparisonValue);
        }
    }
}