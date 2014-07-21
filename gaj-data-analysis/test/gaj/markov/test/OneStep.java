package gaj.markov.test;

import gaj.analysis.markov.ArraySequenceProbs;
import gaj.data.markov.ProbabilityType;
import gaj.data.markov.SequenceProbabilities;
import gaj.data.markov.SequenceType;

public class OneStep {

	public static void main(String[] args) {
		double[] probInitStates = new double[]{ 0.9, 0.1 };
		double[] probFinalStates = new double[]{ 0.1, 0.5 };
		double[] probStates = new double[]{ 0.5, 0.5 };
		double[][] probTransitionStates = new double[][]{
				{ 0.1, 0.4}, { 0.4, 0.1 }	
		}; // => P(s_t|s_{t-1}) = [0.2 0.8; 0.8 0.2].
		OneStep markov = new OneStep(probInitStates, probFinalStates,
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
		display("P(s_2|s_1)", markov.forwardStateTransitions());
		double[][] alpha = markov.forwardLikelihoods(sequenceProbs);
		display("alpha", alpha);
		double[][] beta = markov.backwardLikelihoods(sequenceProbs);
		display("beta", beta);

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

}
