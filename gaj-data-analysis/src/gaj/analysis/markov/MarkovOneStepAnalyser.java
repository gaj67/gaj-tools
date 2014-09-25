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
	switch (type) {
	    case End:
		return posteriorProbabilities(obsProbs, stateProbs, finalProbs, transProbs);
	    case Full:
		return posteriorProbabilities(obsProbs, initProbs, finalProbs, transProbs);
	    case Start:
		return posteriorProbabilities(obsProbs, initProbs, onesProbs, transProbs);
	    case Sub:
		return posteriorProbabilities(obsProbs, stateProbs, onesProbs, transProbs);
	    default:
		throw new IllegalArgumentException("Unknown sequence type: " + type);
	}
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
	DataVector finalProbs = computeTerminalStateProbabilities(nonFinalCounts, finalCounts);
	DataVector stateProbs = computeArbitraryStateProbabilities(nonFinalCounts, finalCounts);
	DataMatrix transProbs = computeStateTransitionProbabilities(transCounts);
	return new MarkovOneStepAnalyser(initProbs, finalProbs, stateProbs, transProbs);
    }

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

    private static DataVector computeTerminalStateProbabilities(DataVector nonFinalCounts, DataVector finalCounts) {
	WritableVector probs = VectorFactory.newVector(finalCounts);
	final int numStates = finalCounts.size();
	for (int state = 0; state < numStates; state++) {
	    double finalCount = finalCounts.get(state);
	    if (finalCount > 0) {
		probs.multiply(state, 1.0 / (finalCount + nonFinalCounts.get(state)));
	    }
	}
	return probs;
    }

    private static DataVector computeArbitraryStateProbabilities(DataVector nonFinalCounts, DataVector finalCounts) {
	WritableVector probs = VectorFactory.newVector(nonFinalCounts);
	probs.add(finalCounts);
	probs.multiply(1.0 / probs.sum());
	return probs;
    }

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
     * Computes the forward joint probabilities, p(x_1,...,x_t,s_t|start),
     * of a known sequence {x_t}
     * of observations, for the unknown state s_t
     * at each stage t=1,2,...,T.
     *
     * @param obsProbs - The T x S matrix of conditional
     * observation probabilities, p(x_t|s_t).
     * @param startProbs - The length-S vector of
     * initial state probabilities, P(s_1|start).
     * @param transProbs - The S x S matrix of state transition
     * probabilities, P(s_t|s_{t-1}), with rows indexed by s_{t-1}
     * and columns indexed by s_t.
     * @return The T x S matrix of forward probabilities.
     */
    public static DataMatrix forwardProbabilities(
	    DataMatrix obsProbs, DataVector startProbs,
	    DataMatrix transProbs)
    {
	// Initialise forward probabilities, p(x_1,...,x_t,s_t).
	final int numStages = obsProbs.numRows();
	final int numStates = obsProbs.numColumns();
	WritableMatrix mp = MatrixFactory.newMatrix(numStages, numStates);
	// Compute alpha_1 = p(x_1,s_1) = p(x_1|s_1) P(s_1|start).
	DataVector alpha = VectorFactory.multiply(obsProbs.getRow(0), startProbs);
	mp.setRow(0, alpha);
	// Compute p(x_1,...,x_{t-1},s_t)
	//  = sum_{s_{t-1}}p(x_1,...,x_{t-1},s_{t-1}) P(s_t|s_{t-1}).
	//  = sum_{s_{t-1}}alpha_{t-1} P(s_t|s_{t-1}).
	// Compute alpha_t = p(x_1,...,x_t,s_t)
	//  = p(x_t|s_t) p(x_1,...,x_{t-1},s_t).
	for (int t = 1; t < numStages; t++) {
	    // Compute stage t quantities using stage t-1.
	    alpha = VectorFactory.multiply(
		    obsProbs.getRow(t),
		    MatrixFactory.multiply(alpha, transProbs));
	    mp.setRow(t, alpha);
	}
	return mp;
    }

    /**
     * Computes the backward conditional probabilities,
     * p(x_{t+1},...,x_T,end|s_t),
     * of a known sequence {x_t}
     * of observations, for the unknown state s_t
     * at each stage t=1,2,...,T.
     *
     * @param obsProbs - The T x S matrix of conditional
     * observation probabilities, p(x_t|s_t).
     * @param endProbs - The length-S vector of
     * terminating conditional state probabilities, P(end|s_T).
     * @param transProbs - The S x S matrix of state transition
     * probabilities, P(s_t|s_{t-1}), with rows indexed by s_{t-1}
     * and columns indexed by s_t.
     * @return The T x S matrix of backward probabilities.
     */
    public static DataMatrix backwardProbabilities(
	    DataMatrix obsProbs, DataVector endProbs, DataMatrix transProbs)
    {
	// Initialise backward probabilities, p(x_{t+1},...,x_T|s_t).
	final int numStages = obsProbs.numRows();
	WritableMatrix mp = MatrixFactory.newMatrix(numStages, obsProbs.numColumns());
	// Set beta_T = P(end|s_T).
	DataVector beta = endProbs;
	int t = numStages - 1;
	mp.setRow(t, beta);
	while (t > 0) {
	    // Compute beta_{t-1} = p(x_t,...,x_T|s_{t-1})
	    //  = sum_{s_t} p(x_t,...,x_T|s_t) P(s_t|s_{t-1}).
	    //  = sum_{s_t} p(x_{t+1},...,x_T|s_t) p(x_t|s_t) P(s_t|s_{t-1}).
	    //  = sum_{s_t} beta_t p(x_t|s_t) P(s_t|s_{t-1}).
	    beta = MatrixFactory.multiply(
		    transProbs,
		    VectorFactory.multiply(beta, obsProbs.getRow(t)));
	    mp.setRow(--t, beta);
	}
	return mp;
    }

    /**
     * Computes the joint probabilities,
     * p(x_1,...,x_T,end,s_t|start),
     * of a known sequence {x_t}
     * of observations, for the unknown state s_t
     * at each stage t=1,2,...,T.
     *
     * @param obsProbs - The T x S matrix of conditional
     * observation probabilities, p(x_t|s_t).
     * @param startProbs - The length-S vector of
     * initial, prior state probabilities, P(s_1|start).
     * @param endProbs - The length-S vector of
     * terminating conditional state probabilities, P(end|s_T).
     * @param transProbs - The S x S matrix of state transition
     * probabilities, P(s_t|s_{t-1}), with rows indexed by s_{t-1}
     * and columns indexed by s_t.
     * @return The T x S matrix of joint probabilities.
     */
    public static DataMatrix jointProbabilities(
	    DataMatrix obsProbs,
	    DataVector startProbs, DataVector endProbs,
	    DataMatrix transProbs)
    {
	// Compute forward probabilities, alpha_t = p(x_1,...,x_t,s_t).
	WritableMatrix mp = (WritableMatrix) forwardProbabilities(obsProbs, startProbs, transProbs);
	final int numStages = mp.numRows();
	// Compute joint probability,
	//   p(x_1,...,x_T,end,s_T|start) = alpha_T * beta_T.
	int t = numStages - 1;
	DataVector beta = endProbs;
	mp.multiplyRow(t, beta);
	while (t > 0) {
	    // Compute beta_{t-1} = p(x_t,...,x_T|s_{t-1})
	    //  = sum_{s_t} p(x_t,...,x_T|s_t) P(s_t|s_{t-1}).
	    //  = sum_{s_t} p(x_{t+1},...,x_T|s_t) p(x_t|s_t) P(s_t|s_{t-1}).
	    //  = sum_{s_t} beta_t p(x_t|s_t) P(s_t|s_{t-1}).
	    beta = MatrixFactory.multiply(
		    transProbs,
		    VectorFactory.multiply(beta, obsProbs.getRow(t)));
	    // Compute joint probability,
	    //   p(x_1,...,x_T,end,s_t|start) = alpha_t * beta_t.
	    mp.multiplyRow(--t, beta);
	}
	return mp;
    }

    /**
     * Computes the state posterior probabilities,
     * p(s_t|start,x_1,...,x_T,end),
     * of a known sequence {x_t}
     * of observations, for the unknown state s_t
     * at each stage t=1,2,...,T.
     *
     * @param obsProbs - The T x S matrix of conditional
     * observation probabilities, p(x_t|s_t).
     * @param startProbs - The length-S vector of
     * initial, prior state probabilities, P(s_1|start).
     * @param endProbs - The length-S vector of
     * terminating conditional state probabilities, P(end|s_T).
     * @param transProbs - The S x S matrix of state transition
     * probabilities, P(s_t|s_{t-1}), with rows indexed by s_{t-1}
     * and columns indexed by s_t.
     * @return The T x S matrix of posterior probabilities.
     */
    public static DataMatrix posteriorProbabilities(
	    DataMatrix obsProbs,
	    DataVector startProbs, DataVector endProbs,
	    DataMatrix transProbs)
    {
	WritableMatrix mp = (WritableMatrix) jointProbabilities(obsProbs, startProbs, endProbs, transProbs);
	double norm = 1.0 / mp.getRow(0).sum(); // Compute normaliser, 1 / p(x_1,...,x_T).
	mp.multiply(norm);
	return mp;
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
