package gaj.analysis.markov;

import gaj.data.markov.SequenceType;
import gaj.data.matrix.DataMatrix;
import gaj.data.matrix.WritableMatrix;
import gaj.data.vector.DataVector;
import gaj.data.vector.WritableVector;
import gaj.impl.matrix.MatrixFactory;
import gaj.impl.vector.VectorFactory;

/**
 * Provides the basic methods for analysing one-step Markov sequences.
 * <p/>Note: It is assumed for the static methods that all sequences are full,
 * i.e. have a definite start and end.
 * For sequences with an unknown start, use P(s_t) in place of P(s_1|start).
 * For sequences with an unknown end, use a ones-vector in place of P(end|s_T).
 */
public class MarkovOneStepAnalyser {

    //**********************************************************************
    // Dynamic methods.

    /**
     * The initial state probabilities, P(s_1|start), of stage 1 (the first stage).
     */
    private final DataVector initProbs;
    /**
     * The terminal state probabilities, P(end|s_n), of stage n (the last stage).
     */
    private final DataVector finalProbs;
    /**
     * The state probabilities, P(s_i), of arbitrary stage i.
     * Used when a sequence has no definite start.
     */
    private final DataVector stateProbs;
    /**
     * Pseudo-terminal state probabilities used when a sequence has no definite end.
     */
    private final DataVector onesProbs;
    /**
     * The S x S matrix of non-terminal state transition
     * probabilities, P(s_t|s_{t-1}), with rows indexed by s_{t-1}
     * and columns indexed by s_t.
     */
    private final DataMatrix transProbs;

    /**
     * @see {@link #newAnalyser}().
     */
    private MarkovOneStepAnalyser(DataVector initProbs, DataVector finalProbs,
	    DataVector stateProbs, DataMatrix transProbs)
    {
	this.initProbs = initProbs;
	this.finalProbs = finalProbs;
	this.stateProbs = stateProbs;
	this.onesProbs = VectorFactory.newVector(initProbs.size());
	this.transProbs = transProbs;
    }

    /**
     * Computes the state posterior probabilities,
     * p(s_t|start,x_1,...,x_T,end),
     * of a known sequence or sub-sequence {x_t}
     * of observations, for the unknown state s_t
     * at each stage t=1,2,...,T.
     *
     * @param obsProbs - The T x S matrix of conditional
     * observation probabilities, p(x_t|s_t).
     * @param type - The type of sequence or sub-sequence.
     * @return The T x S matrix of posterior probabilities.
     */
    public DataMatrix posteriorProbabilities(DataMatrix obsProbs, SequenceType type) {
	DataVector startProbs = type.isInitiated() ? initProbs : stateProbs;
	DataVector endProbs = type.isTerminated() ? finalProbs : onesProbs;
	return MarkovOneStepLibrary.posteriorProbabilities(obsProbs, startProbs, endProbs, transProbs);
    }

    //**********************************************************************
    // Static methods.

    /**
     * Initialises a one-step Markov analyser with the necessary state probabilities.
     *
     * @param initCounts - A length-S vector of the number of times each state
     * occurred at the beginning of a sequence with a known start.
     * @param finalCounts - A length-S vector of the number of times each state
     * occurred at the end of a sequence with a known end.
     * @param transCounts - An S x S matrix of the number of times state s_{t-1}
     * was followed by state s_t, where s_{t-1} specifies the row and st specifies the column.
     * @return An initialised Markov analsyer.
     */
    public static MarkovOneStepAnalyser newAnalyser(
	    DataVector initCounts, DataVector finalCounts,
	    DataMatrix transCounts)
    {
	DataVector initProbs = computeInitialStateProbabilities(initCounts);
	DataVector nonFinalCounts = computeNonTerminalStateTransitionCounts(transCounts);
	DataVector stateCounts = computeStateCounts(nonFinalCounts, finalCounts);
	DataVector finalProbs = computeTerminalStateProbabilities(stateCounts, finalCounts);
	DataVector stateProbs = computeArbitraryStateProbabilities(stateCounts);
	DataMatrix transProbs = computeStateTransitionProbabilities(transCounts);
	return new MarkovOneStepAnalyser(initProbs, finalProbs, stateProbs, transProbs);
    }

    // Normalises C(s_1|start) to give P(s_1|start).
    private static DataVector computeInitialStateProbabilities(DataVector initCounts) {
	WritableVector probs = VectorFactory.newVector(initCounts);
	probs.multiply(1.0 / initCounts.sum());
	return probs;
    }

    /*
     * Computes the number of transitions from each state to another (or the same),
     * non-terminal state.
     */
    private static DataVector computeNonTerminalStateTransitionCounts(DataMatrix transCounts) {
	final int numStates = transCounts.numRows();
	WritableVector probs = VectorFactory.newVector(numStates);
	for (int row = 0; row < numStates; row++) {
	    probs.set(row, transCounts.getRow(row).sum());
	}
	return probs;
    }

    // Computes C(s), the number of times state s occurred at any stage.
    private static DataVector computeStateCounts(DataVector nonFinalCounts, DataVector finalCounts) {
	WritableVector counts = VectorFactory.newVector(nonFinalCounts);
	counts.add(finalCounts);
	return counts;
    }

    // Computes P(end|s_T) = C(end|s_T) / C(s_T).
    private static DataVector computeTerminalStateProbabilities(DataVector stateCounts, DataVector finalCounts) {
	final int numStates = finalCounts.size();
	WritableVector probs = VectorFactory.newVector(numStates);
	for (int state = 0; state < numStates; state++) {
	    double finalCount = finalCounts.get(state);
	    if (finalCount > 0) {
		probs.set(state, finalCount / stateCounts.get(state));
	    }
	}
	return probs;
    }

    // Normalises C(s) to give P(s).
    private static DataVector computeArbitraryStateProbabilities(DataVector stateCounts) {
	WritableVector probs = VectorFactory.newVector(stateCounts);
	probs.multiply(1.0 / probs.sum());
	return probs;
    }

    // Normalises C(s_{t-1},s_t) to give P(s_t|s_{t-1}).
    private static DataMatrix computeStateTransitionProbabilities(DataMatrix transCounts) {
	WritableMatrix probs = MatrixFactory.newMatrix(transCounts);
	final int numStates = transCounts.numRows();
	for (int row = 0; row < numStates; row++) {
	    double nonFinalCounts = transCounts.getRow(row).sum();
	    if (nonFinalCounts > 0) {
		probs.multiplyRow(row, 1.0 / nonFinalCounts);
	    }
	}
	return probs;
    }

    /**
     * Computes the state transition probabilities, P(s_t|s_{t-1}), from the joint state
     * probabilities, P(s_t,s_{t-1}).
     *
     * @param jointProbs - The S x S matrix of joint probabilities, ordered by s_{t-1} down the
     * columns and s_t across the rows.
     * @return The S x S matrix of transition probabilities, ordered by s_{t-1} down the
     * columns and s_t across the rows.
     */
    public static DataMatrix computeTransitions(DataMatrix jointProbs) {
	WritableMatrix transProbs = MatrixFactory.newMatrix(jointProbs);
	MatrixFactory.normaliseRowSums(transProbs);
	return transProbs;
    }
}
