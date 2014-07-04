package gaj.analysis.markov;

import gaj.data.markov.SequenceProbabilities;

import java.util.Random;

import utils.markov.SequenceProbabilities.ProbabilityType;
import utils.markov.SequenceProbabilities.SequenceType;

public class MarkovState1 {

	private double[] vP_s1_g_start;
	private double[] vP_end_g_sT;
	private double[] vP_st;
	private double[][] mP_st_g_stm1;
	private int numStates;

	/**
	 * @param probInitStates - The initial probability vector, P(s_1|start), of starting a sequence with state s_1.
	 * @param probFinalStates - The final probability vector, P(end|s_T), of terminating a sequence from state s_T.
	 * @param probStates - The probability vector, P(s_t), of being in state s_t at stage t.
	 * @param probTransitionStates - The conditional probability matrix, P(s_t|s_{t-1}), of
	 * transitioning from state s_{t-1} at stage t-1 to state s_t at stage t.
	 */
	public MarkovState1(double[] probInitStates,
			double[] probFinalStates,
			double[] probStates,
			double[][] probTransitionStates
			) {
		vP_s1_g_start = probInitStates;
		vP_end_g_sT = probFinalStates;
		vP_st = probStates;
		mP_st_g_stm1 = probTransitionStates;
		numStates = probInitStates.length;
	}

	/**
	 * @param sequenceProbs - The densities, p(x_t|s_t), of observation x_t given state s_t,
	 * for each stage t=1,2,...,T.
	 * @return The forward probabilities, p(x_1,...,x_t,s_t), for stages t=1,2,...,T.
	 */
	public double[][] forwardLikelihoods(SequenceProbabilities sequenceProbs)
	{
		// Initialise forward probabilities, p(x_1,...,x_t,s_t).
		final int numStages = sequenceProbs.numStages();
		double[][] mp_x1_xt_st = new double[numStages][numStates];
		// Compute p(x_1,s_1) = p(x_1|s_1)P(s_1).
		double[] vp_x1_g_s1 = sequenceProbs.stageVector(1);
		double[] vP_s1 = sequenceProbs.sequenceType().isInitiated()
				? vP_s1_g_start : vP_st;
		double[] vp_x1_s1 = mp_x1_xt_st[0];
		for (int s1 = 0; s1 < numStates; s1++)
			vp_x1_s1[s1] = vp_x1_g_s1[s1] * vP_s1[s1];
		// Compute p(x_1,...,x_{t-1},s_t)
		//  = sum_{s_{t-1}}p(x_1,...,x_{t-1},s_{t-1))P(s_t|s_{t-1}).
		// Compute alpha_t = p(x_1,...,x_t,s_t)
		//  = p(x_1,...,x_{t-1},s_t)p(x_t|s_t).
		double[] vp_x1_xtm1_stm1 = vp_x1_s1;
		for (int t = 1; t < numStages; t++) {
			// Compute stage t quantities using stage t-1.
			double[] vp_xt_g_st = sequenceProbs.stageVector(t+1);
			double[] vp_x1_xt_st = mp_x1_xt_st[t];
			for (int s_t = 0; s_t < numStates; s_t++) {
				double[] vP_st_g_stm1 = mP_st_g_stm1[s_t];
				double sp_x1_xtm1_st = 0.0;
				for (int s_tm1 = 0; s_tm1 < numStates; s_tm1++)
					sp_x1_xtm1_st += vP_st_g_stm1[s_tm1] * vp_x1_xtm1_stm1[s_tm1];
				vp_x1_xt_st[s_t] = sp_x1_xtm1_st * vp_xt_g_st[s_t];
			}
			// Demote current stage t to past stage t-1 for next iteration.
			vp_x1_xtm1_stm1 = vp_x1_xt_st;
		}
		return mp_x1_xt_st;
	}

	/**
	 * @param sequenceProbs - The densities, p(x_t|s_t), of observation x_t given state s_t,
	 * for each stage t=1,2,...,T.
	 * @return The backward probabilities, p(x_{t+1},...,x_T|s_t), for stages t=1,2,...,T.
	 */
	public double[][] backwardLikelihoods(SequenceProbabilities sequenceProbs)
	{
		// Initialise backward probabilities, p(x_{t+1},...,x_T|s_t).
		final int numStages = sequenceProbs.numStages();
		double[][] mp_xtp1_xT_g_st = new double[numStages][numStates];
		// Set p(x_{T+1}|s_T).
		double[] vp_xTp1_g_sT = mp_xtp1_xT_g_st[numStages-1];
		if (sequenceProbs.sequenceType().isTerminated()) {
			for (int s_T = 0; s_T < numStates; s_T++)
				vp_xTp1_g_sT[s_T] = vP_end_g_sT[s_T];
		} else {
			for (int s_T = 0; s_T < numStates; s_T++)
				vp_xTp1_g_sT[s_T] = 1.0;
		}
		// Compute beta_{t-1} = p(x_t,...,x_T|s_{t-1})
		//  = sum_{s_t}p(x_{t+1},...,x_T|s_t)p(x_t|s_t)P(s_t|s_{t-1}).
		double[] vp_xtp1_xT_g_st = vp_xTp1_g_sT;
		double[] vp_xt_xT_g_st = new double[numStates];
		for (int t = numStages-1; t > 0; t--) {
			// Compute stage t quantities using stage t+1.
			double[] vp_xt_g_st = sequenceProbs.stageVector(t+1);
			for (int s_t = 0; s_t < numStates; s_t++) {
				vp_xt_xT_g_st[s_t] = vp_xtp1_xT_g_st[s_t] * vp_xt_g_st[s_t];
			}
			double[] vp_xt_xT_g_stm1 = mp_xtp1_xT_g_st[t-1];
			for (int s_t = 0; s_t < numStates; s_t++) {
				double[] vp_st_g_stm1 = mP_st_g_stm1[s_t];
				double sp_xt_xT_g_st = vp_xt_xT_g_st[s_t];
				for (int s_tm1 = 0; s_tm1 < numStates; s_tm1++) {
					vp_xt_xT_g_stm1[s_tm1] += sp_xt_xT_g_st * vp_st_g_stm1[s_tm1];
				}
			}
			// Demote current stage t to future stage t+1 for next iteration.
			vp_xtp1_xT_g_st = vp_xt_xT_g_stm1;
		}
		return mp_xtp1_xT_g_st;
	}

	/**
	 * @param sequenceProbs - The densities, p(x_t|s_t), of observation x_t given state s_t,
	 * for each stage t=1,2,...,T.
	 * @return The joint probabilities, p(x_1,...,x_T,s_t), for stages t=1,2,...,T.
	 */
	public double[][] jointLikelihoods(SequenceProbabilities sequenceProbs)
	{
		// Compute backward probabilities first.
		double[][] mp_xtp1_xT_g_st = backwardLikelihoods(sequenceProbs);
		// Reuse storage for joint probabilities.
		double[][] mp_x1_xT_st = mp_xtp1_xT_g_st;
		// Initialise forward probabilities, p(x_1,...,x_t,s_t).
		final int numStages = sequenceProbs.numStages();
		// Compute p(x_1,s_1) = p(x_1|s_1)P(s_1).
		double[] vp_x1_g_s1 = sequenceProbs.stageVector(1);
		double[] vP_s1 = sequenceProbs.sequenceType().isInitiated()
				? vP_s1_g_start : vP_st;
		double[] vp_x1_s1 = new double[numStates];
		double[] vp_x1_xT_st = mp_x1_xT_st[0];
		for (int s1 = 0; s1 < numStates; s1++) {
			vp_x1_s1[s1] = vp_x1_g_s1[s1] * vP_s1[s1];
			vp_x1_xT_st[s1] *= vp_x1_s1[s1];
		}
		// Compute p(x_1,...,x_{t-1},s_t)
		//  = sum_{s_{t-1}}p(x_1,...,x_{t-1},s_{t-1))P(s_t|s_{t-1}).
		// Compute alpha_t = p(x_1,...,x_t,s_t)
		//  = p(x_1,...,x_{t-1},s_t)p(x_t|s_t).
		double[] vp_x1_xtm1_stm1 = vp_x1_s1;
		double[] vp_x1_xt_st = new double[numStates];
		for (int t = 1; t < numStages; t++) {
			// Compute stage t quantities using stage t-1.
			double[] vp_xt_g_st = sequenceProbs.stageVector(t+1);
			vp_x1_xT_st = mp_x1_xT_st[t];
			for (int s_t = 0; s_t < numStates; s_t++) {
				double[] vP_st_g_stm1 = mP_st_g_stm1[s_t];
				double sp_x1_xtm1_st = 0.0;
				for (int s_tm1 = 0; s_tm1 < numStates; s_tm1++)
					sp_x1_xtm1_st += vP_st_g_stm1[s_tm1] * vp_x1_xtm1_stm1[s_tm1];
				vp_x1_xt_st[s_t] = sp_x1_xtm1_st * vp_xt_g_st[s_t];
				vp_x1_xT_st[s_t] *= vp_x1_xt_st[s_t];
			}
			// Demote current stage t to past stage t-1 for next iteration.
			double[] tmp = vp_x1_xtm1_stm1;
			vp_x1_xtm1_stm1 = vp_x1_xt_st;
			vp_x1_xt_st = tmp;
		}
		return mp_x1_xT_st;
	}

	/**
	 * @param sequenceProbs - The densities, p(x_t|s_t), of observation x_t given state s_t,
	 * for each stage t=1,2,...,T.
	 * @return The posterior state probabilities, p(s_t|x_1,...,x_T), for stages t=1,2,...,T.
	 */
	public double[][] posteriorStates(SequenceProbabilities sequenceProbs)
	{
		double[][] probs = jointLikelihoods(sequenceProbs);
		double sp_x1_xT = 0.0;
		double[] vp_x1_xT_st = probs[0];
		for (int s_t = 0; s_t < numStates; s_t++)
			sp_x1_xT += vp_x1_xT_st[s_t];
		double norm = 1.0 / sp_x1_xT;
		for (double[] vprobs : probs) {
			for (int s_t = 0; s_t < numStates; s_t++)
				vprobs[s_t] *= norm;
		}
		return probs;
	}

	static final Random generator = new Random();
	public int[] sampleSequence(final int maxNumStages) {
		// Initialise sequence with null states.
		int[] states = new int[maxNumStages];
		for (int t = 0; t < maxNumStages; t++)
			states[t] = -1;
		// Initialise possible initial states.
		double[] vP_st = new double[numStates];
		for (int s_t = 0; s_t < numStates; s_t++)
			vP_st[s_t] = vP_s1_g_start[s_t];
		// Simple relabelling.
		double[][] mP_stp1_g_st = mP_st_g_stm1;
		double[] vP_stp1 = vP_st;
		// Generate sequence of states.
		for (int t = 0; t < maxNumStages; t++) {
			int s_t = states[t] = sampleState(vP_st);
			if (generator.nextDouble() < vP_end_g_sT[s_t])
				break; // Terminate sequence.
			// Transition to stage t+1.
			for (int s_tp1 = 0; s_tp1 < numStates; s_tp1++)
				vP_stp1[s_tp1] = mP_stp1_g_st[s_tp1][s_t];
		}
		return states;
	}

	private int sampleState(double[] vP_st) {
		double chance = generator.nextDouble();
		int s_t = 0;
		for (double sP_st : vP_st) {
			if (chance < sP_st) return s_t;
			chance -= sP_st;
			s_t++;
		}
		return -1;
	}

	//****************************************************
	public static void main(String[] args) {
		double[] probInitStates = new double[]{ 0.9, 0.1 };
		double[] probFinalStates = new double[]{ 0.1, 0.5 };
		double[] probStates = new double[]{ 0.5, 0.5 };
		double[][] probTransitionStates = new double[][]{
				{ 0.2, 0.8}, { 0.8, 0.2 }
		};
		MarkovState1 markov = new MarkovState1(probInitStates, probFinalStates,
				probStates, probTransitionStates);
		double[][] observationProbs = new double[][]{
				{ 0.8, 0.3 }, { 0.4, 0.9 }
		};
		SequenceProbabilities sequenceProbs = new ArraySequenceProbs(observationProbs, SequenceType.full, ProbabilityType.likelihood);
		/*
		 *  Checks:
		 *    p(x_1,s_1|start) = [0.9*0.8, 0.1*0.3] = [0.72, 0.03].
		 *    p(x_1,s_2|start) = [0.72*0.2+0.03*0.8, 0.72*0.8+0.03*0.2]
		 *                     = [0.168, 0.582].
		 *    p(x_1,x_2,s_2|start) = [0.168*0.4, 0.582*0.9] = [0.0672, 0.5238].
		 *    p(x_1,x_2,s_2,end|start) = [0.0672*0.1, 0.5238*0.5] = [0.00672, 0.2619].
		 *
		 *    p(end|s_2) = [0.1, 0.5].
		 *    p(x_2,end|s_2) = [0.1*0.4, 0.5*0.9] = [0.04, 0.45].
		 *    p(x_2,end|s_1) = [0.04*0.2+0.45*0.8, 0.04*0.8+0.45*0.2] = [0.368, 0.122].
		 */
		double[][] alpha = markov.forwardLikelihoods(sequenceProbs);
		display("alpha", alpha);
		double[][] beta = markov.backwardLikelihoods(sequenceProbs);
		display("beta", beta);
		double[][] joint = markov.jointLikelihoods(sequenceProbs);
		display("joint", joint);
		for (int t = 0; t < sequenceProbs.numStages(); t++) {
			for (int s_t = 0; s_t < markov.numStates; s_t++)
				joint[t][s_t] = alpha[t][s_t] * beta[t][s_t];
		}
		display("alpha*beta", joint);
		display("posteriors", markov.posteriorStates(sequenceProbs));
		display("sample", markov.sampleSequence(10));
		display("sample", markov.sampleSequence(10));
		display("sample", markov.sampleSequence(10));
		display("sample", markov.sampleSequence(10));
		display("sample", markov.sampleSequence(10));
	}

	private static void display(String label, double[][] matrix) {
		System.out.printf("%s=[\n", label);
		for (double[] row : matrix) {
			for (double value : row)
				System.out.printf("%6.4f,", value);
			System.out.println();
		}
		System.out.println("]");
	}

	@SuppressWarnings("unused")
	private static void display(String label, double[] vector) {
		System.out.printf("%s=[ ", label);
		for (double value : vector) {
			System.out.printf("%6.4f ", value);
		}
		System.out.println("]");
	}

	private static void display(String label, int[] vector) {
		System.out.printf("%s=[ ", label);
		for (int value : vector) {
			System.out.printf("%d ", value);
		}
		System.out.println("]");
	}

}
