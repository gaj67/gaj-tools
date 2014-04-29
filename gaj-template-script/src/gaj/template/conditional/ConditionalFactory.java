/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.conditional;

import gaj.template.data.ScriptData;


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
public class ConditionalFactory {

    private static final String START_TYPE_STRING = "[";
    private static final String END_TYPE_STRING = "]";

    private ConditionalFactory() {}

    /**
     * Parses the conditional statement into a fully-fledged object that can be
     * (re)used for testing. <br/>The statement takes the form:
     * <pre>
     * [ "[" <tt><em>type</em></tt> "]" ] <tt><em>variable</em></tt> ( <tt><em>unary-comparator</em></tt> | <tt><em>binary-comparator</em></tt> <tt><em>value</em></tt> )
     * </pre>
     *
     * @param conditional - The conditional statement.
     * @return A new Conditional instance, or a value of null if the conditional
     * statement is invalid.
     * @see Type {@link Type} &nbsp;- for more information about type
     * conversion.
     * @see Comparator {@link Comparator} &nbsp;- for more information about
     * unary and binary comparators.
     */
    public static Evaluator parseConditional(String conditional) {
        Object[] parts = Comparator.parse(conditional);
        String variable = (String) parts[0];
        Comparator comparator = (Comparator) parts[1];
        String value = (String) parts[2];
        // Handle special cases - see evaluate().
        //XXX: The following statement crashes on the null!
        //Boolean junctionFlag = (Comparator.IS_EQUAL == comparator) ? true /*disjunction*/
        //                       : ((Comparator.NOT_EQUAL == comparator) ? false /*conjunction*/ : null /*neither*/);
        Boolean junctionFlag = null; // By default, neither disjunction nor conjunction.
        if (Comparator.IS_EQUAL == comparator) {
            junctionFlag = true; // Disjunction.
        } else if (Comparator.NOT_EQUAL == comparator) {
            junctionFlag = false; // Conjunction.
        }
        String[] values = (junctionFlag == null) ? null : value.split("[|]");
        if (variable.startsWith(START_TYPE_STRING)) {
            int idx = variable.indexOf(END_TYPE_STRING);
            if (idx < 0) {
                throw new ConditionalParseException("Broken variable type delcaration: " + conditional);//TODO
            }
            String typeString = variable.substring(START_TYPE_STRING.length(), idx);
            for (Type type : Type.values()) {
                if (type.value.equals(typeString)) {
                    return new Conditional(type, variable.substring(idx + 1), comparator, value, values, junctionFlag);
                }
            }
            throw new ConditionalParseException("Unknown variable type delcaration: " + conditional);//TODO
        } else {
            return new Conditional(null, variable, comparator, value, values, junctionFlag);
        }
    }

    /**
     * Creates a multi-conditional instance that always evaluates to a fixed answer.
     * 
     * @param isTrue - A Boolean flag indicating the outcome of a call to {@link MultiConditional#evaluate}(). 
     * @return A fixed-evaluation multi-conditional object.
     */
    public static Evaluator newFixedConditional(final boolean isTrue) {
        return new Evaluator() {
            @Override
            public boolean evaluate(ScriptData data) {
                return isTrue;
            }
        };
    }

    /**
     * Creates a multi-conditional instance that evaluates multiple conditional instances in order, treating them
     * as either conjunctions or disjunctions.
     * The {@link MultiConditional#evaluate}() method uses short-cut evaluation, and will halt as soon as the answer
     * has been determined.
     * 
     * @param isConj - A Boolean flag indicating whether to treat the conditionals as conjunctions (true)
     * or disjunctions (false).
     * @param conditionals - An array of conditionals
     * @return A conjunctive or disjunctive multi-conditional object.
     */
    public static Evaluator newMultiConditional(boolean isConj, Evaluator... conditionals) {
        return new MultiConditional(isConj, conditionals);
    }

}