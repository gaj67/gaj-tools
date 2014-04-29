/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.conditional;

import gaj.template.data.ScriptData;


public interface Evaluator {

    /**
     * Determines whether a conditional evaluates to true or false for the
     * given properties.
     *
     * @param data - The contextual properties.
     * @return A value of true (or false) if the conditional does (or does not)
     * evaluate to true for the given data.
     */
    public boolean evaluate(ScriptData data);
    
}
