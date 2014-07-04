package gaj.analysis.markov;

import gaj.data.markov.ProbabilityType;
import gaj.data.markov.SequenceProbabilities;
import gaj.data.markov.SequenceType;


public class ArraySequenceProbs implements SequenceProbabilities {

	private final double[][] probs;
	private final SequenceType seqType;
	private final ProbabilityType probType;
	private final int numStages, numStates;

	public ArraySequenceProbs(double[][] seqProbs, SequenceType seqType, ProbabilityType probType) {
		probs = seqProbs;
		this.seqType = seqType;
		this.probType = probType;
		numStages = probs.length;
		numStates = (numStages == 0) ? 0 : probs[0].length;
	}
	
	@Override
	public int numStages() {
		return numStages;
	}

	@Override
	public int numStates() {
		return numStates;
	}

	@Override
	public SequenceType sequenceType() {
		return seqType;
	}

	@Override
	public ProbabilityType probabilityType() {
		return probType;
	}

	@Override
	public double[] stageVector(int t) {
		return probs[t-1];
	}

}
