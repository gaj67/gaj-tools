package gaj.analysis.markov;

import gaj.analysis.matrix.MatrixFactory;
import gaj.analysis.vector.VectorFactory;
import gaj.data.matrix.DataMatrix;
import gaj.data.matrix.WritableMatrix;
import gaj.data.vector.DataVector;

/**
 * Provides the basic methods for analysing 
 * hidden Markov sequences.
 */
public abstract class MarkovFactory {

	private MarkovFactory() {}
	
	/**
	 * Computes the forward joint probabilities, p(x_1,...,x_t,s_t),
	 * of a known sequence {x_t}
	 * of observations, for the unknown state s_t
	 * at each stage t=1,2,...,T.
	 * 
	 * @param obsProbs - The T x S matrix of conditional 
	 * observation probabilities, p(x_t|s_t).
	 * @param startProbs - The length-S vector of 
	 * initial state probabilities, P(s_1|start).
	 * @param transProbs - The S x S matrix of state transition
	 * probabilities, P(s_t|s_{t-1}), with rows indexed by s_{t-1}.
	 * @return The T x S matrix of forward probabilities.
	 */
	public static DataMatrix forwardProbabilities(
			DataMatrix obsProbs, DataVector startProbs,
			DataMatrix transProbs) 
	{
		// Initialise forward probabilities, p(x_1,...,x_t,s_t).
		final int numStages = obsProbs.numRows();
		final int numStates = obsProbs.numColumns();
		WritableMatrix mp = MatrixFactory.newWritableMatrix(numStages, numStates);
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
	 * p(x_{t+1},...,x_T|s_t),
	 * of a known sequence {x_t}
	 * of observations, for the unknown state s_t
	 * at each stage t=1,2,...,T.
	 * 
	 * @param obsProbs - The T x S matrix of conditional 
	 * observation probabilities, p(x_t|s_t).
	 * @param endProbs - The length-S vector of 
	 * terminating conditional state probabilities, P(end|s_T).
	 * @param transProbs - The S x S matrix of state transition
	 * probabilities, P(s_t|s_{t-1}), with rows indexed by s_{t-1}.
	 * @return The T x S matrix of backward probabilities.
	 */
	public static DataMatrix backwardProbabilities(
			DataMatrix obsProbs, DataVector endProbs, DataMatrix transProbs) 
	{
		// Initialise backward probabilities, p(x_{t+1},...,x_T|s_t).
		final int numStages = obsProbs.numRows();
		final int numStates = obsProbs.numColumns();
		WritableMatrix mp = MatrixFactory.newWritableMatrix(numStages, numStates);
		// Set beta_T = P(end|s_T).
		DataVector beta = endProbs;
		int t = numStages - 1;
		mp.setRow(t, beta);
		while (t > 0) {
			// Compute beta_{t-1} = p(x_t,...,x_T|s_{t-1})
			//  = sum_{s_t}p(x_t,...,x_T|s_t) P(s_t|s_{t-1}).
			//  = sum_{s_t}p(x_{t+1},...,x_T|s_t) p(x_t|s_t) P(s_t|s_{t-1}).
			//  = sum_{s_t}beta_t p(x_t|s_t) P(s_t|s_{t-1}).
			beta = MatrixFactory.multiply(
					transProbs, 
					VectorFactory.multiply(beta, obsProbs.getRow(t)));
			mp.setRow(--t, beta);
		}
		return mp;
	}

	/**
	 * Computes the backward conditional probabilities, 
	 * p(x_{t+1},...,x_T|s_t),
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
	 * probabilities, P(s_t|s_{t-1}), with rows indexed by s_{t-1}.
	 * @return The T x S matrix of backward probabilities.
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
		mp.setRow(t, VectorFactory.multiply(mp.getRow(t), beta));
		while (t > 0) {
			// Compute beta_{t-1} = p(x_t,...,x_T|s_{t-1})
			//  = sum_{s_t}p(x_t,...,x_T|s_t) P(s_t|s_{t-1}).
			//  = sum_{s_t}p(x_{t+1},...,x_T|s_t) p(x_t|s_t) P(s_t|s_{t-1}).
			//  = sum_{s_t}beta_t p(x_t|s_t) P(s_t|s_{t-1}).
			beta = MatrixFactory.multiply(
					transProbs, 
					VectorFactory.multiply(beta, obsProbs.getRow(t)));
			// Compute joint probability, 
			//   p(x_1,...,x_T,end,s_t|start) = alpha_t * beta_t.
			mp.setRow(--t, VectorFactory.multiply(mp.getRow(t), beta));
		}
		return mp;
	}
}
