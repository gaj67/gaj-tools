package gaj.analysis.markov;

import gaj.data.markov.SequenceType;
import gaj.data.matrix.DataMatrix;
import gaj.data.matrix.WritableMatrix;
import gaj.data.vector.DataVector;
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
     * was followed by state s_t, where s_{t-1} specifies the row and s_t specifies the column.
     * @return An initialised Markov analsyer.
     */
    public static MarkovOneStepAnalyser newAnalyser(
	    DataVector initCounts, DataVector finalCounts,
	    DataMatrix transCounts)
    {
	// Normalises C(s_1|start) to give P(s_1|start).
	DataVector initProbs = VectorFactory.divide(initCounts, initCounts.sum());
	// Computes C_{nonterm}(s_j) = sum_{s_{j+1}} C(s_j, s_{j+1}).
	DataVector nonFinalCounts = MatrixFactory.sumColumns(transCounts);
	// Computes C(s_j) = C_{nonterm}(s_j) + C_{term}(s_j).
	DataVector stateCounts = VectorFactory.add(nonFinalCounts, finalCounts);
	// Computes P(end|s_T) = C(end|s_T) / C(s_T).
	DataVector finalProbs = VectorFactory.divide(finalCounts, stateCounts);
	// Normalises C(s_j) to give P(s_j).
	DataVector stateProbs = VectorFactory.divide(stateCounts, stateCounts.sum());
	// Computes P(s_{j+1}|s_j) = C(s_j, s_{j+1}) / C_{nonterm}(s_j).
	DataMatrix transProbs = MatrixFactory.divideRows(transCounts, nonFinalCounts);
	return new MarkovOneStepAnalyser(initProbs, finalProbs, stateProbs, transProbs);
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
