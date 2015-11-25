package gaj.analysis.classifier;

import gaj.data.classifier.GoldData;
import gaj.data.vector.DataVector;
import java.util.HashSet;
import java.util.Set;

/**
 * Scores the proportion of correctly classified data, allowing for ties.
 */
public class AccuracyScorer extends BaseScorer {

    private final double tieTolerance;

    /**
     * Binds the scorer to the given data, with tied classifications assessed according to
     * the given tolerance. That is, the set of ties is given by {c | P_max-P(c|x) < tol}.
     * 
     * @param data - The gold-standard data.
     * @param tieTolerance - The tolerance between a classification probability and the
     * maximum probability, below which a possible tie is indicated.
     */
    public AccuracyScorer(GoldData data, double tieTolerance) {
        super(data);
        this.tieTolerance = tieTolerance;
    }

    @Override
    protected double getScore(DataVector probs, int classIndex) {
        Set<Integer> ties = getTies(probs);
        return (ties.contains(classIndex)) ? 1.0 / ties.size() : 0;
    }

    private Set<Integer> getTies(DataVector probs) {
        int maxIdx = -1;
        double maxProb = Double.NEGATIVE_INFINITY;
        int idx = -1;
        for (double prob : probs) {
            idx++;
            if (prob > maxProb) {
                maxProb = prob;
                maxIdx = idx;
            }
        }
        Set<Integer> ties = new HashSet<>();
        ties.add(maxIdx);
        if (tieTolerance > 0) {
            idx = -1;
            for (double prob : probs) {
                idx++;
                if (maxProb - prob < tieTolerance) {
                    ties.add(idx);
                }
            }
        }
        return ties;
    }

}
