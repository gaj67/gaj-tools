package gaj.markov.test;

import static org.junit.Assert.assertTrue;
import gaj.analysis.markov.MarkovOneStepAnalyser;
import gaj.analysis.markov.MarkovOneStepLibrary;
import gaj.data.markov.SequenceType;
import gaj.data.matrix.DataMatrix;
import gaj.data.vector.DataVector;
import gaj.impl.matrix.MatrixFactory;
import gaj.impl.vector.VectorFactory;
import org.junit.Test;

public class MarkovTest {

    /** C(s_t, s_{t-1}). */
    private static final DataMatrix transCounts = MatrixFactory.newMatrix(
	    new double[][]{
		    { 1, 4}, // C(s_1=sigma_1,s_2)
		    { 4, 1 } // C(s_1=sigma_2,s_2)
	    });
    private static final double EPSILON = 1e-6;
    /** P(s_t|s_{t-1}). */
    private static final DataMatrix transProbs =
	    MarkovOneStepAnalyser.computeTransitions(transCounts);
    /** P(s_1|start). */
    private static final DataVector startProbs = VectorFactory.newVector(0.9, 0.1);
    /** P(end|s_T). */
    private static final DataVector endProbs = VectorFactory.newVector(0.1, 0.5);
    /** p(x_t|s_t). */
    private static final DataMatrix obsProbs = MatrixFactory.newMatrix(
	    new double[][]{
		    { 0.8, 0.3 }, // p(x_1|s_1)
		    { 0.4, 0.9 }  // p(x_2|s_2)
	    });

    private boolean equals(double x, double y) {
	return Math.abs(x - y) < EPSILON;
    }

    @Test
    public void testTransitionProbs() {
	System.out.println("testTransitionProbs:");
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
    }

    @Test
    public void testForwardAlgo() {
	System.out.println("testForwardAlgo:");
	DataMatrix alpha = MarkovOneStepLibrary.forwardProbabilities(obsProbs, startProbs, transProbs);
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
    }

    @Test
    public void testBackwardAlgo() {
	System.out.println("testBackwardAlgo:");
	DataMatrix beta = MarkovOneStepLibrary.backwardProbabilities(obsProbs, endProbs, transProbs);
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
    }

    @Test
    public void testForwardBackwardAlgo() {
	System.out.println("testForwardBackwardAlgo:");
	DataMatrix gamma = MarkovOneStepLibrary.jointProbabilities(obsProbs, startProbs, endProbs, transProbs);
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
    }

    @Test
    public void testNormedForwardBackwardAlgo() {
	System.out.println("testNormedForwardBackwardAlgo:");
	DataMatrix pred = MarkovOneStepLibrary.posteriorProbabilities(obsProbs, startProbs, endProbs, transProbs);
	MatrixFactory.display("p(s_t|start,x_1,...,x_N,end)=", pred, "\n");
	/*
	 *  Posterior checks:
	 *    p(s_1|start,x_1,x_2,end) = [0.26496, 0.00366]/0.26862
	 *                             = [0.98637480..., 0.01362519...].
	 *    p(s_2|start,x_1,x_2,end) = [0.00672, 0.2619]/0.26862
	 *                             = [0.02501675..., 0.97498324...].
	 */
	DataMatrix expectedPred = MatrixFactory.newMatrix(
		new double[][] {
			{ 0.986375, 0.013625 },
			{ 0.025017, 0.974983 }
		}
		);
	assertTrue(MatrixFactory.equals(expectedPred, pred, EPSILON));
	assertTrue(Math.abs(pred.getRow(0).sum() - 1) <= EPSILON);
	assertTrue(Math.abs(pred.getRow(1).sum() - 1) <= EPSILON);
    }

    @Test
    public void testAnalyser() {
	System.out.println("testAnalyser:");
	/*
	 * Fudge the probabilities to produce the various state transition counts.
	 */
	DataVector initCounts = VectorFactory.scale(startProbs, 10);
	VectorFactory.display("C(s_1|start)=", initCounts, "\n");
	DataVector finalCounts = VectorFactory.newVector(5.0/9, 5);
	VectorFactory.display("C(end|s_N)=", finalCounts, "\n");
	MarkovOneStepAnalyser analyser = MarkovOneStepAnalyser.newAnalyser(
		initCounts, finalCounts, transCounts);
	DataMatrix pred = analyser.posteriorProbabilities(obsProbs, SequenceType.Complete);
	MatrixFactory.display("p(s_t|start,x_1,...,x_N,end)=", pred, "\n");

	DataMatrix expectedPred = MatrixFactory.newMatrix(
		new double[][] {
			{ 0.986375, 0.013625 },
			{ 0.025017, 0.974983 }
		});
	assertTrue(MatrixFactory.equals(expectedPred, pred, EPSILON));
    }

}
