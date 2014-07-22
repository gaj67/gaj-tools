package gaj.markov.test;

import static org.junit.Assert.*;
import gaj.analysis.markov.MarkovOneStep;
import gaj.data.matrix.DataMatrix;
import gaj.data.vector.DataVector;
import gaj.impl.matrix.MatrixFactory;
import gaj.impl.vector.VectorFactory;

import org.junit.Test;

public class MarkovTest {

	private static final double EPSILON = 1e-6;

	private boolean equals(double x, double y) {
		return Math.abs(x - y) < EPSILON;
	}
	
	@Test
	public void test() {
		DataMatrix jointProbs = MatrixFactory.newMatrix( // P(s_{t-1},s_t)
			new double[][]{
				{ 0.1, 0.4}, // P(s_1=sigma_1,s_2)
				{ 0.4, 0.1 } // P(s_1=sigma_2,s_2)
			}
		);
		DataMatrix transProbs = MarkovOneStep.computeTransitions(jointProbs); // P(s_t|s_{t-1})
		DataMatrix expectedTransProbs = MatrixFactory.newMatrix(
			new double[][]{
				{ 0.2, 0.8}, // P(s_2|s_1=sigma_1)
				{ 0.8, 0.2 } // P(s_2|s_1=sigma_2)
			}
		);
		MatrixFactory.display("P(s_t|s_{t-1})=", transProbs, "\n");
		assertTrue(MatrixFactory.equals(expectedTransProbs, transProbs, EPSILON));
		assertTrue(equals(transProbs.get(0, 0), 0.2));
		assertTrue(equals(transProbs.get(0, 1), 0.8));
		assertTrue(equals(transProbs.get(1, 0), 0.8));
		assertTrue(equals(transProbs.get(1, 1), 0.2));

		DataVector startProbs = VectorFactory.newVector(0.9, 0.1); // P(s_0)
		DataMatrix obsProbs = MatrixFactory.newMatrix( // p(x_t|s_t)
			new double[][]{
				{ 0.8, 0.3 }, // p(x_1|s_1)
				{ 0.4, 0.9 }  // p(x_2|s_2)
			}
		);
		DataMatrix alpha = MarkovOneStep.forwardProbabilities(obsProbs, startProbs, transProbs);
		MatrixFactory.display("p(x_1,x_2,...,x_t,s_t|start)=", alpha, "\n");
		/*
		 *  Forward checks: 
		 *    p(x_1,s_1|start) = [0.9*0.8, 0.1*0.3] = [0.72, 0.03].
		 *    p(x_1,s_2|start) = [0.72*0.2+0.03*0.8, 0.72*0.8+0.03*0.2]
		 *                     = [0.168, 0.582].
		 *    p(x_1,x_2,s_2|start) = [0.168*0.4, 0.582*0.9] = [0.0672, 0.5238].
		 */
		DataMatrix expectedAlpha = MatrixFactory.newMatrix(
			new double[][] {
				{ 0.72, 0.03 },
				{ 0.0672, 0.5238 }
			}
		);
		assertTrue(MatrixFactory.equals(expectedAlpha, alpha, EPSILON));

		DataVector endProbs = VectorFactory.newVector(0.1, 0.5);   // P(end|s_N)
		DataMatrix beta = MarkovOneStep.backwardProbabilities(obsProbs, endProbs, transProbs);
		MatrixFactory.display("p(x_{t+1},x_{t+2},...,end|s_t,start)=", beta, "\n");
		/*
		 *  Backward checks:
		 *    p(end|s_2) = [0.1, 0.5].
		 *    p(x_2,end|s_2) = [0.1*0.4, 0.5*0.9] = [0.04, 0.45].
		 *    p(x_2,end|s_1) = [0.04*0.2+0.45*0.8, 0.04*0.8+0.45*0.2] = [0.368, 0.122].
		 */
		DataMatrix expectedBeta = MatrixFactory.newMatrix(
			new double[][] {
				{ 0.368, 0.122 },
				{ 0.1, 0.5 }
			}
		);
		assertTrue(MatrixFactory.equals(expectedBeta, beta, EPSILON));

		DataMatrix gamma = MarkovOneStep.jointProbabilities(obsProbs, startProbs, endProbs, transProbs);
		MatrixFactory.display("p(x_1,...,x_N,end,s_t|start)=", gamma, "\n");
		/*
		 *  Joint checks:
		 *    p(x_1,x_2,end,s_1|start) = [0.368*0.72, 0.122*0.03] = [0.26496, 0.00366].
		 *    p(x_1,x_2,end,s_2|start) = [0.1*0.0672, 0.5*0.5238] = [0.00672, 0.2619].
		 */
		DataMatrix expectedGamma = MatrixFactory.newMatrix(
			new double[][] {
				{ 0.26496, 0.00366 },
				{ 0.00672, 0.2619 }
			}
		);
		assertTrue(MatrixFactory.equals(expectedGamma, gamma, EPSILON));
		double norm = gamma.getRow(0).sum();
		assertTrue(Math.abs(norm - gamma.getRow(1).sum()) <= EPSILON);

		DataMatrix pred = MarkovOneStep.posteriorProbabilities(obsProbs, startProbs, endProbs, transProbs);
		MatrixFactory.display("p(s_t|start,x_1,...,x_N,end)=", pred, "\n");
		assertTrue(Math.abs(pred.getRow(0).sum() - 1) <= EPSILON);
		assertTrue(Math.abs(pred.getRow(1).sum() - 1) <= EPSILON);
		assertTrue(Math.abs(pred.get(0, 0)*norm - gamma.get(0, 0)) <= EPSILON);
	
	}

}