package gaj.analysis.markov;

import utils.markov.SequenceProbabilities.SequenceType;

public class Test {

	public static void main(String[] args) {
		SequenceType seqType = SequenceType.start;
		System.out.printf("initiated=%s\n", seqType.isInitiated());
		System.out.printf("terminated=%s\n", seqType.isTerminated());
	}

}
