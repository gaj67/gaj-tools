/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.segment;

import gaj.template.conditional.Evaluator;
import gaj.template.data.ScriptData;

public class ConditionalSegment extends CommandSegment implements Evaluator {

    private final Evaluator conditional;

    /*package-private*/ ConditionalSegment(CommandType type, Evaluator conditional) {
        super(type);
        this.conditional = conditional;
    }
    
    @Override
    public boolean evaluate(ScriptData data) {
        return conditional.evaluate(data);
    }

}
