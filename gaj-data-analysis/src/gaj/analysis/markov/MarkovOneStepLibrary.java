package gaj.analysis.markov;

import java.util.List;
import gaj.analysis.data.numeric.matrix.DataMatrix;
import gaj.analysis.data.numeric.matrix.WritableMatrix;
import gaj.analysis.data.numeric.matrix.impl.MatrixFactory;
import gaj.analysis.data.numeric.vector.DataVector;
import gaj.analysis.data.numeric.vector.IndexVector;
import gaj.analysis.data.numeric.vector.impl.VectorFactory;

/**
 * Provides the basic methods for analysing one-step Markov sequences.
 *
 * <p/>
 * Note 1: It is assumed here that all sequences are <em>complete</em> in the sense that they have a definite start and end. For example, a complete sequence of states from stage 1
 * to stage T is represented as &lt;s_1, ..., s_t&gt;. In contrast, a sequence with a definite start but an indefinite end, known as a <em>start</em> sequence, is represented as
 * &lt;s_1,...,s_T], and a sequence with an indefinite start but a definite end, known as an <em>end</em> sequence, is represented as [s_1,...,s_T&gt;. Lastly, a sequence with an
 * indefinite start and end, known as an <em>incomplete</em> sequence, is represented as [s_1,...,s_T].
 * <p/>
 * Now, observe that P(&lt;s_1,s_2,...) = P(s_1|&lt;)P(s_2,...|s_1), whereas P(s_1,s_2,...) = P(s_1)P(s_2,...|s_1). Hence, this module can handle sequences that do <b>not</b> have
 * a definite start by replacing P(s_1|&lt;) with P(s), the probability of an arbitrary state s at any arbitrary stage. Similarly, observe that P(...,s_T&gt;) =
 * P(&gt;|s_T)P(...,s_T). Hence, this module can also handle sequences that do <b>not</b> have a definite end by replacing P(&gt;|s_T) by a vector of ones.
 *
 * <p/>
 * Note 2: Although this module models arbitrary T-length sequences, it does not take into account the distribution of T. Thus, P(s_1,...,s_T) is more strictly taken to be the
 * conditional probability P(s_1,...,s_T|T) rather than the joint probability P(s_1,...,s_T, T).
 *
 * <p/>
 * Note 3: For the purposes of generality, this module does not directly model the internal probabilities of a sequence x_1,...,x_T of observations, but instead assumes that
 * p(x_t|s_t) is given. Hence, the module can analyse both univariate and multivariate observations, as well as discrete or continuous observations, without assuming any specific
 * knowledge. However, as a consequence, the training of hidden state Markov sequences will require additional, external functionality, although the methods in this module will
 * suffice once the various state distributions have been determined.
 */
public abstract class MarkovOneStepLibrary {

    private MarkovOneStepLibrary() {
    }

    /**
     * Computes the forward joint probabilities, p(&lt;x_1,...,x_t], s_t),
     * of a known sequence &lt;x_1,...,x_T&gt;
     * of observations, for the unknown state s_t
     * at each stage t=1,2,...,T.
     *
     * @param obsProbs - The T x S matrix of conditional
     * observation probabilities, p(x_t|s_t).
     * @param startProbs - The length-S vector of
     * initial state probabilities, P(s_1|&lt;).
     * @param transProbs - The S x S matrix of state transition
     * probabilities, P(s_t|s_{t-1}), with rows indexed by s_{t-1}
     * and columns indexed by s_t.
     * @return The T x S matrix of forward probabilities.
     */
    public static DataMatrix forwardProbabilities(
            DataMatrix obsProbs, DataVector startProbs,
            DataMatrix transProbs)
    {
        final int numStages = obsProbs.numRows();
        final int numStates = obsProbs.numColumns();
        WritableMatrix mp = MatrixFactory.newMatrix(numStages, numStates);
        // Compute alpha_1 = p(x_1,s_1) = p(x_1|s_1) P(s_1|<).
        DataVector alpha = VectorFactory.multiply(obsProbs.getRow(0), startProbs);
        mp.setRow(0, alpha);
        // Compute p(<x_1,...,x_{t-1}], s_t)
        // = sum_{s_{t-1}}p(<x_1,...,x_{t-1}], s_{t-1}) P(s_t|s_{t-1}).
        // = sum_{s_{t-1}}alpha_{t-1} P(s_t|s_{t-1}).
        // Compute alpha_t = p(<x_1,...,x_t], s_t)
        // = p(x_t|s_t) p(<x_1,...,x_{t-1}], s_t).
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
     * p([x_{t+1},...,x_T&gt;|s_t),
     * of a known sequence &lt;x_1,...,x_T&gt;
     * of observations, for the unknown state s_t
     * at each stage t=1,2,...,T.
     *
     * @param obsProbs - The T x S matrix of conditional
     * observation probabilities, p(x_t|s_t).
     * @param endProbs - The length-S vector of
     * terminating conditional state probabilities, P(&gt;|s_T).
     * @param transProbs - The S x S matrix of state transition
     * probabilities, P(s_t|s_{t-1}), with rows indexed by s_{t-1}
     * and columns indexed by s_t.
     * @return The T x S matrix of backward probabilities.
     */
    public static DataMatrix backwardProbabilities(
            DataMatrix obsProbs, DataVector endProbs, DataMatrix transProbs)
    {
        final int numStages = obsProbs.numRows();
        WritableMatrix mp = MatrixFactory.newMatrix(numStages, obsProbs.numColumns());
        // Set beta_T = P(>|s_T).
        DataVector beta = endProbs;
        int t = numStages - 1;
        mp.setRow(t, beta);
        while (t > 0) {
            // Compute beta_{t-1} = p([x_t,...,x_T>|s_{t-1})
            // = sum_{s_t} p([x_t,...,x_T>|s_t) P(s_t|s_{t-1}).
            // = sum_{s_t} p([x_{t+1},...,x_T>|s_t) p(x_t|s_t) P(s_t|s_{t-1}).
            // = sum_{s_t} beta_t p(x_t|s_t) P(s_t|s_{t-1}).
            beta = MatrixFactory.multiply(
                    transProbs,
                    VectorFactory.multiply(beta, obsProbs.getRow(t)));
            mp.setRow(--t, beta);
        }
        return mp;
    }

    /**
     * Computes the joint probabilities,
     * p(&lt;x_1,...,x_T&gt; ,s_t),
     * of a known sequence &lt;x_1,...,x_T&gt;
     * of observations, for the unknown state s_t
     * at each stage t=1,2,...,T.
     *
     * @param obsProbs - The T x S matrix of conditional
     * observation probabilities, p(x_t|s_t).
     * @param startProbs - The length-S vector of
     * initial, prior state probabilities, P(s_1|&lt;).
     * @param endProbs - The length-S vector of
     * terminating conditional state probabilities, P(&gt;|s_T).
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
        // Compute forward probabilities, alpha_t = p(<x_1,...,x_t>, s_t).
        WritableMatrix mp = (WritableMatrix) forwardProbabilities(obsProbs, startProbs, transProbs);
        final int numStages = mp.numRows();
        // Compute joint probability,
        // p(<x_1,...,x_T>, s_T) = alpha_T * beta_T.
        int t = numStages - 1;
        DataVector beta = endProbs;
        mp.multiplyRow(t, beta);
        while (t > 0) {
            // Compute beta_{t-1} = p([x_t,...,x_T>|s_{t-1})
            // = sum_{s_t} p([x_t,...,x_T>|s_t) P(s_t|s_{t-1}).
            // = sum_{s_t} p([x_{t+1},...,x_T>|s_t) p(x_t|s_t) P(s_t|s_{t-1}).
            // = sum_{s_t} beta_t p(x_t|s_t) P(s_t|s_{t-1}).
            beta = MatrixFactory.multiply(
                    transProbs,
                    VectorFactory.multiply(beta, obsProbs.getRow(t)));
            // Compute joint probability,
            // p(<x_1,...,x_T>, s_t) = alpha_t * beta_t.
            mp.multiplyRow(--t, beta);
        }
        return mp;
    }

    /**
     * Computes the state posterior probabilities,
     * p(s_t|&lt;x_1,...,x_T&gt;),
     * of a known sequence &lt;x_1,...,x_T&gt;
     * of observations, for the unknown state s_t
     * at each stage t=1,2,...,T.
     *
     * @param obsProbs - The T x S matrix of conditional
     * observation probabilities, p(x_t|s_t).
     * @param startProbs - The length-S vector of
     * initial, prior state probabilities, P(s_1|&lt;).
     * @param endProbs - The length-S vector of
     * terminating conditional state probabilities, P(&gt;|s_T).
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
        // Compute normaliser, 1 / p(<x_1,...,x_T>).
        double norm = 1.0 / mp.getRow(0).sum();
        mp.multiply(norm);
        return mp;
    }

    /**
     * Computes the prior probability P(&lt;s_1,...,s_T&gt;|T)
     * of a given sequence of states.
     *
     * @param startProbs - The length-S vector of
     * initial, prior state probabilities, P(s_1|&lt;).
     * @param endProbs - The length-S vector of
     * terminating conditional state probabilities, P(&gt;|s_T).
     * @param transProbs - The S x S matrix of state transition
     * probabilities, P(s_t|s_{t-1}), with rows indexed by s_{t-1}
     * and columns indexed by s_t.
     * @param stateSequence - The sequence of state indices {k_t},
     * where s_t=sigma_{k_t} for each stage t=1,...,T.
     * @return The prior state sequence probability.
     */
    public static double priorProbability(
            DataVector startProbs, DataVector endProbs,
            DataMatrix transProbs, IndexVector stateSequence)
    {
        double prob = 1;
        final int numStages = stateSequence.size();
        if (numStages > 0) {
            int prevState = stateSequence.get(0);
            // Compute P(s_1|<).
            prob = startProbs.get(prevState);
            for (int stage = 1; stage < numStages; stage++) {
                int nextState = stateSequence.get(stage);
                // Compute P(<s_1,...,s_t]) = P(<s_1,...,s_{t-1}]) P(s_t|s_{t-1}).
                prob *= transProbs.get(prevState, nextState);
                prevState = nextState;
            }
            // Compute P(<s_1,...,s_T>) = P(<s_1,...,s_T]) P(>|s_T).
            prob *= endProbs.get(prevState);
        }
        return prob;
    }

    /**
     * Computes the joint probability P(&lt;s_1,...,s_T&gt;, &lt;x_1,...,x_T&gt;)
     * of a given sequence of states and a given sequence of observations.
     *
     * @param startProbs - The length-S vector of
     * initial, prior state probabilities, P(s_1|&lt;).
     * @param endProbs - The length-S vector of
     * terminating conditional state probabilities, P(&gt;|s_T).
     * @param transProbs - The S x S matrix of state transition
     * probabilities, P(s_t|s_{t-1}), with rows indexed by s_{t-1}
     * and columns indexed by s_t.
     * @param stateSequence - The sequence of state indices {k_t},
     * where s_t=sigma_{k_t} for each stage t=1,...,T.
     * @param obsProbs - The T x S matrix of conditional
     * observation probabilities, p(x_t|s_t).
     * @return The joint state--observation sequence probability.
     */
    public static double jointProbability(
            DataVector startProbs, DataVector endProbs,
            DataMatrix transProbs,
            IndexVector stateSequence, DataMatrix obsProbs)
    {
        double prob = 1;
        final int numStages = stateSequence.size();
        if (numStages > 0) {
            int prevState = stateSequence.get(0);
            // Compute p(s_1,x_1|<) = P(s_1|<) p(x_1|s_1).
            prob = startProbs.get(prevState) * obsProbs.get(0, prevState);
            for (int stage = 1; stage < numStages; stage++) {
                int nextState = stateSequence.get(stage);
                // Compute p(<s_1,...,s_t], <x_1,...,x_t]) =
                // p(<s_1,...,s_{t-1}], <x_1,...,x_{t-1}]) P(s_t|s_{t-1}) p(x_t|s_t).
                prob *= transProbs.get(prevState, nextState) * obsProbs.get(stage, nextState);
                prevState = nextState;
            }
            // Compute p(<s_1,...,s_T>, <x_1,...,x_T>) =
            // P(<s_1,...,s_T], <x_1,...,x_T]) P(>|s_T).
            prob *= endProbs.get(prevState);
        }
        return prob;
    }

    /**
     * Computes the data probability, p(&lt;x_1,...,x_t&gt;),
     * of a known sequence &lt;x_1,...,x_t&gt;
     * of observations, for the unknown state s_t
     * at each stage t=1,2,...,T.
     *
     * @param startProbs - The length-S vector of
     * initial, prior state probabilities, P(s_1|&lt;).
     * @param endProbs - The length-S vector of
     * terminating conditional state probabilities, P(&gt;|s_T).
     * @param transProbs - The S x S matrix of state transition
     * probabilities, P(s_t|s_{t-1}), with rows indexed by s_{t-1}
     * and columns indexed by s_t.
     * @param obsProbs - The T x S matrix of conditional
     * observation probabilities, p(x_t|s_t).
     * @return The data probability.
     */
    public static double dataProbability(
            DataVector startProbs,
            DataVector endProbs,
            DataMatrix transProbs, DataMatrix obsProbs)
    {
        final int numStages = obsProbs.numRows();
        // Compute alpha_1 = p(x_1,s_1|<) = p(x_1|s_1) P(s_1|<).
        DataVector alpha = VectorFactory.multiply(obsProbs.getRow(0), startProbs);
        // Compute p(<x_1,...,x_{t-1}], s_t)
        // = sum_{s_{t-1}} p(<x_1,...,x_{t-1}], s_{t-1}) P(s_t|s_{t-1}).
        // = sum_{s_{t-1}} alpha_{t-1} P(s_t|s_{t-1}).
        // Compute alpha_t = p(<x_1,...,x_t], s_t)
        // = p(x_t|s_t) p(<x_1,...,x_{t-1}], s_t|<).
        for (int t = 1; t < numStages; t++) {
            // Compute stage t quantities using stage t-1.
            alpha = VectorFactory.multiply(
                    obsProbs.getRow(t),
                    MatrixFactory.multiply(alpha, transProbs));
        }
        // Compute p(<x_1,...,x_T>)
        // = sum_{s_T} p(<x_1,...,x_T], s_T) P(>|s_T)
        // = sum_{s_T} alpha_T P(>|s_T).
        return VectorFactory.dot(alpha, endProbs);
    }

    /**
     * Computes the state posterior probability, p(&lt;s_1,...,s_T&gt;|&lt;x_1,...,x_t&gt;),
     * of a known sequence &lt;s_1,...,s_T&gt; of states given the
     * known sequence &lt;x_1,...,x_t&gt;
     * of observations.
     *
     * @param startProbs - The length-S vector of
     * initial, prior state probabilities, P(s_1|&lt;).
     * @param endProbs - The length-S vector of
     * terminating conditional state probabilities, P(&gt;|s_T).
     * @param transProbs - The S x S matrix of state transition
     * probabilities, P(s_t|s_{t-1}), with rows indexed by s_{t-1}
     * and columns indexed by s_t.
     * @param stateSequence - The sequence of state indices {k_t},
     * where s_t=sigma_{k_t} for each stage t=1,...,T.
     * @param obsProbs - The T x S matrix of conditional
     * observation probabilities, p(x_t|s_t).
     * @return The data probability.
     */
    public static double posteriorProbability(
            DataVector startProbs,
            DataVector endProbs,
            DataMatrix transProbs,
            IndexVector stateSequence, DataMatrix obsProbs)
    {
        double prob = 1;
        final int numStages = stateSequence.size();
        if (numStages > 0) {
            // Compute alpha_1 = p(x_1,s_1|<) = p(x_1|s_1) P(s_1|<).
            DataVector alpha = VectorFactory.multiply(obsProbs.getRow(0), startProbs);
            // Compute p(s_1,x_1|<) = P(s_1|<) p(x_1|s_1).
            int prevState = stateSequence.get(0);
            prob = alpha.get(prevState);
            for (int stage = 1; stage < numStages; stage++) {
                // Compute p(<x_1,...,x_{t-1}], s_t)
                // = sum_{s_{t-1}} p(<x_1,...,x_{t-1}], s_{t-1}) P(s_t|s_{t-1}).
                // = sum_{s_{t-1}} alpha_{t-1} P(s_t|s_{t-1}).
                // Compute alpha_t = p(<x_1,...,x_t], s_t)
                // = p(x_t|s_t) p(<x_1,...,x_{t-1}], s_t).
                alpha = VectorFactory.multiply(
                        obsProbs.getRow(stage),
                        MatrixFactory.multiply(alpha, transProbs));
                // Compute p(<s_1,...,s_t], <x_1,...,x_t]) =
                // p(<s_1,...,s_{t-1}], <x_1,...,x_{t-1}]) P(s_t|s_{t-1}) p(x_t|s_t).
                int nextState = stateSequence.get(stage);
                prob *= transProbs.get(prevState, nextState) * obsProbs.get(stage, nextState);
                prevState = nextState;
            }
            // Compute p(<s_1,...,s_T>, <x_1,...,x_T>) =
            // P(<s_1,...,s_T], <x_1,...,x_T]) P(>|s_T).
            prob *= endProbs.get(prevState);
            // Compute P(<s_1,...,s_T>|<x_1,...,x_T>) =
            // p(<s_1,...,s_T>, <x_1,...,x_T>) / p(<x_1,...,x_T>).
            prob /= VectorFactory.dot(alpha, endProbs);
        }
        return prob;
    }

    /**
     * Computes the state sequence &lt;s_1,...,s_T&gt; that
     * maximises the posterior probability, P(&lt;s_1,...,s_T&gt;|&lt;x_1,...,x_T&gt;),
     * for a known sequence of observations &lt;x_1,...,x_T&gt;.
     *
     * @param startProbs - The length-S vector of
     * initial, prior state probabilities, P(s_1|&lt;).
     * @param endProbs - The length-S vector of
     * terminating conditional state probabilities, P(&gt;|s_T).
     * @param transProbs - The S x S matrix of state transition
     * probabilities, P(s_t|s_{t-1}), with rows indexed by s_{t-1}
     * and columns indexed by s_t.
     * @param obsProbs - The T x S matrix of conditional
     * observation probabilities, p(x_t|s_t).
     * @return The most probable state sequence,
     * or a value of null if this is potentially ambiguous.
     */
    public static/* @Nullable */IndexVector getOptimalStateSequence(
            DataVector startProbs,
            DataVector endProbs,
            DataMatrix transProbs, DataMatrix obsProbs)
    {
        final int numStages = obsProbs.numRows();
        int[] stateIndices = new int[numStages];
        DataMatrix alpha = forwardProbabilities(obsProbs, startProbs, transProbs);
        // Start with beta*_T(s_T) = P(>|s_T).
        DataVector beta = endProbs;
        for (int t = numStages - 1; t >= 0; t--) {
            // Compute p(<x_1,...,x_T>, s_t, [s*_{t+1},...,s*_T>)
            // = p(<x_1,...,x_t], s_t) p([x_{t+1},...,x_T>, [s*_{t+1},...,s*_T>|s_t)
            // = alpha_t beta*_t.
            DataVector gamma = VectorFactory.multiply(alpha.getRow(t), beta);
            List<Integer> maxStates = VectorFactory.argMaxes(gamma);
            if (maxStates.size() != 1) {
                // Reject multiplicity.
                return null;
            }
            int maxState = maxStates.get(0);
            stateIndices[t] = maxState;
            if (t > 0) {
                // Compute beta*_{t-1}(s_{t-1})
                // = p([x_t,...,x_T>, [s*_t,...,s*_T>|s_{t-1})
                // = p([x_{t+1},...,x_T>, [s*_{t+1},...,s*_T>|s*_t)
                // * p(x_t|s*_t) P(s*_t|s_{t-1})
                // = beta*_t(s*_t) p(x_t|s*_t) P(s*_t|s_{t-1}).
                beta = VectorFactory.scale(
                        transProbs.getColumn(maxState),
                        beta.get(maxState) * obsProbs.get(t, maxState));
            }
        }
        return VectorFactory.newIndexVector(stateIndices);
    }

}
