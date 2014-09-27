package gaj.entropy.test;

import gaj.analysis.entropy.EntropyAnalyser;

import java.util.Random;

/**
 * Models the Bayesian network X -> Y (read here as X causes Y)
 * of two discrete nodes, X and Y, where both X and Y take state 0 or 1. 
 * Tests different entropy measures, after ensuring that the paired states of X and Y
 * are positively correlated.
 * That is, Y=X should be more likely than Y=1-X.
 */
public class SimpleBayesNet {

	private static final Random generator = new Random();

	private static final class Model {
		private final double pX0 = generator.nextDouble(); // P(X=0).
		private final double c0 = 0.6 + 0.4 * generator.nextDouble(); // Strength of 0-0 correlation.
		private final double pY0g0 = c0 + (1-c0) * generator.nextDouble(); // P(Y=0|X=0).
		private final double c1 = 0.6 + 0.4 * generator.nextDouble(); // Strength of 1-1 correlation.
		private final double pY1g1 = c1 + (1-c1) * generator.nextDouble(); // P(Y=1|X=1).
	}
	
	public static void main(String[] args) {
		final int M = 1000;
		final int[] oX = new int[M];
		final int[] oY = new int[M];
		final int N = 1;
		for (int i = 0; i < N; i++) {
			Model model = new Model();
			sample(model, oX, oY, 0, M);
			analyse(oX, oY);
		}
	}

	private static void sample(Model model, int[] oX, int[] oY,  
			int start, final int length) 
	{
		int pos = start;
		for (int i = 0; i < length; i++) {
			double pX = generator.nextDouble();
			int vX = (pX <= model.pX0) ? 0 : 1;
			oX[pos] = vX;
			double pY = generator.nextDouble();
			int vY = (vX == 0) 
					? ((pY <= model.pY0g0) ? 0 : 1)
				    : ((pY <= model.pY1g1) ? 1 : 0);
			oY[pos++] = vY;
		}
	}

	private static void analyse(int[] oX, int[] oY) {
		double hX = EntropyAnalyser.entropy(oX);
		double hY = EntropyAnalyser.entropy(oY);
		double hXY = EntropyAnalyser.entropy(oX, oY);
		System.out.printf("H(X)=%f, H(Y)=%f, H(X,Y)=%f%n", hX, hY, hXY);
		System.out.printf("H(X,Y)-H(X)-H(Y)=%f%n", hXY - hX - hY);
		System.out.printf("H(X|Y)=%f, H(X)-H(X|Y)=%f%n", hXY - hY, hX + hY - hXY);
		System.out.printf("H(Y|X)=%f, H(Y)-H(Y|X)=%f%n", hXY - hX, hX + hY - hXY);
	}

}
