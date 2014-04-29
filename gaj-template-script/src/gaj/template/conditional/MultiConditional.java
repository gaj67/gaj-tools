/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.conditional;

import gaj.template.data.ScriptData;

/**
 * Evaluates a sequence of conditionals as either disjunctions or conjunctions.
 * The evaluation halts as soon as a definite result is known.
 *
 * <br/>Note: An empty sequence always evaluates to false.
 */
/*package-private*/ class MultiConditional implements Evaluator {

    private final boolean isConj;
    private final Evaluator[] conditionals;

    /*package-private*/ MultiConditional(boolean isConj, Evaluator[] conditionals) {
        this.isConj = isConj;
        this.conditionals = conditionals;
    }

    @Override
    public boolean evaluate(ScriptData data) {
        if (conditionals.length == 0) return false; // XXX: This is inconsistent with algo. below, but desired behaviour.
        if (isConj) { // Conjunctive form.
            for (Evaluator cond : conditionals) {
                if (!cond.evaluate(data)) return false;
            }
            return true;
        } else { // Disjunctive form.
            for (Evaluator cond : conditionals) {
                if (cond.evaluate(data)) return true;
            }
            return false;
        }
    }
}
