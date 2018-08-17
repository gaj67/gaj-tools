package gaj.markov.test;

import static org.junit.Assert.assertTrue;
import gaj.analysis.markov.MarkovOneStepAnalyser;
import gaj.analysis.markov.MarkovOneStepLibrary;
import gaj.analysis.markov.SequenceType;
import gaj.analysis.numeric.matrix.DataMatrix;
import gaj.analysis.numeric.matrix.impl.MatrixFactory;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.IndexVector;
import gaj.analysis.numeric.vector.impl.VectorFactory;
import org.junit.Test;

public class MarkovTest {

    /** C(s_t, s_{t-1}). */
    private static final DataMatrix transCounts = MatrixFactory.newMatrix(
            new double[][] {
                    { 1, 4 }, // C(s_1=sigma_1,s_2)
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
    /** Model p(x_t|s_t) for a length-2 sequence. */
    private static final DataMatrix obsProbs2 = MatrixFactory.newMatrix(
            new double[][] {
                    { 0.8, 0.3 }, // p(x_1|s_1)
                    { 0.4, 0.9 }  // p(x_2|s_2)
            });
    private static final IndexVector stateSequence3 = VectorFactory.newIndexVector(0, 0, 1);
    /** Model p(x_t|s_t) for a length-3 sequence. */
    private static final DataMatrix obsProbs3 = MatrixFactory.newMatrix(
            new double[][] {
                    { 0.8, 0.3 }, // p(x_1|s_1)
                    { 0.4, 0.9 }, // p(x_2|s_2)
                    { 0.5, 0.5 }  // p(x_3|s_3)
            });

    private boolean equals(double x, double y) {
        return Math.abs(x - y) < EPSILON;
    }

    @Test
    public void testTransitionProbs() {
        System.out.println("testTransitionProbs:");
        DataMatrix expectedTransProbs = MatrixFactory.newMatrix(
                new double[][] {
                        { 0.2, 0.8 }, // P(s_2|s_1=sigma_1)
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
        DataMatrix alpha = MarkovOneStepLibrary.forwardProbabilities(obsProbs2, startProbs, transProbs);
        MatrixFactory.display("p(<x_1,x_2,...,x_t], s_t)=", alpha, "\n");
        /*
         * Forward checks:
         * p(x_1,s_1|start) = [0.9*0.8, 0.1*0.3] = [0.72, 0.03].
         * p(x_1,s_2|start) = [0.72*0.2+0.03*0.8, 0.72*0.8+0.03*0.2]
         * = [0.168, 0.582].
         * p(x_1,x_2,s_2|start) = [0.168*0.4, 0.582*0.9] = [0.0672, 0.5238].
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
        DataMatrix beta = MarkovOneStepLibrary.backwardProbabilities(obsProbs2, endProbs, transProbs);
        MatrixFactory.display("p([x_{t+1},x_{t+2},...,x_T>|s_t)=", beta, "\n");
        /*
         * Backward checks:
         * p(end|s_2) = [0.1, 0.5].
         * p(x_2,end|s_2) = [0.1*0.4, 0.5*0.9] = [0.04, 0.45].
         * p(x_2,end|s_1) = [0.04*0.2+0.45*0.8, 0.04*0.8+0.45*0.2] = [0.368, 0.122].
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
        DataMatrix gamma = MarkovOneStepLibrary.jointProbabilities(obsProbs2, startProbs, endProbs, transProbs);
        MatrixFactory.display("p(<x_1,...,x_T>, s_t)=", gamma, "\n");
        /*
         * Joint checks:
         * p(x_1,x_2,end,s_1|start) = [0.368*0.72, 0.122*0.03] = [0.26496, 0.00366].
         * p(x_1,x_2,end,s_2|start) = [0.1*0.0672, 0.5*0.5238] = [0.00672, 0.2619].
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
        DataMatrix pred = MarkovOneStepLibrary.posteriorProbabilities(obsProbs2, startProbs, endProbs, transProbs);
        MatrixFactory.display("p(s_t|<x_1,...,x_T>)=", pred, "\n");
        /*
         * Posterior checks:
         * p(s_1|start,x_1,x_2,end) = [0.26496, 0.00366]/0.26862
         * = [0.98637480..., 0.01362519...].
         * p(s_2|start,x_1,x_2,end) = [0.00672, 0.2619]/0.26862
         * = [0.02501675..., 0.97498324...].
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
    public void testAnalyserPosterior() {
        System.out.println("testAnalyserPosterior:");
        MarkovOneStepAnalyser analyser = getAnalyser();
        DataMatrix pred = analyser.posteriorProbabilities(obsProbs2, SequenceType.Complete);
        MatrixFactory.display("p(s_t|<x_1,...,x_T>)=", pred, "\n");

        DataMatrix expectedPred = MatrixFactory.newMatrix(
                new double[][] {
                        { 0.986375, 0.013625 },
                        { 0.025017, 0.974983 }
                });
        assertTrue(MatrixFactory.equals(expectedPred, pred, EPSILON));
    }

    @Test
    public void testAnalyserStates() {
        System.out.println("testAnalyserStates:");
        MarkovOneStepAnalyser analyser = getAnalyser();
        double prob = analyser.priorProbability(stateSequence3, SequenceType.Start);
        System.out.printf("p(<s_1,...,s_T])=%f%n", prob);
        /*
         * Expected value:
         * P(<s_1=0,s_2=0,s_3=1]) = P(s_1=0|<) P(s_2=0|s_1=0) P(s_3=1|s_2=0).
         */
        double expectedProb = startProbs.get(0) * transProbs.get(0, 0) * transProbs.get(0, 1);
        assertTrue(equals(expectedProb, prob, EPSILON));
        prob = analyser.priorProbability(stateSequence3, SequenceType.Complete);
        /*
         * Expected value:
         * P(<s_1=0,s_2=0,s_3=1>) = P(<s_1=0,s_2=0,s_3=1]) P(>|s_3=1).
         */
        System.out.printf("p(<s_1,...,s_T>)=%f%n", prob);
        expectedProb *= endProbs.get(1);
        assertTrue(equals(expectedProb, prob, EPSILON));
    }

    private MarkovOneStepAnalyser getAnalyser() {
        /*
         * Fudge the probabilities to produce the various state transition counts.
         */
        DataVector initCounts = VectorFactory.scale(startProbs, 10);
        DataVector finalCounts = VectorFactory.newVector(5.0 / 9, 5);
        MarkovOneStepAnalyser analyser = MarkovOneStepAnalyser.newAnalyser(
                initCounts, finalCounts, transCounts);
        return analyser;
    }

    private boolean equals(double expected, double observed, double epsilon) {
        return Math.abs(observed - expected) <= epsilon;
    }

    @Test
    public void testAnalyserStatesAndObs() {
        System.out.println("testAnalyserStatesAndObs:");
        MarkovOneStepAnalyser analyser = getAnalyser();
        double prob = analyser.jointProbability(stateSequence3, obsProbs3, SequenceType.Start);
        System.out.printf("p(<s_1,...,s_T], <x_1,...,x_T])=%f%n", prob);
        /*
         * Expected value:
         * P(<s_1=0,s_2=0,s_3=1], <x_1,x_2,x_3])
         * = P(s_1=0|<) p(x_1|s_1=0)
         * * P(s_2=0|s_1=0) p(x_2|s_2=0)
         * * P(s_3=1|s_2=0) p(x_3|s_3=1).
         */
        double expectedProb = startProbs.get(0) * obsProbs3.get(0, 0)
                * transProbs.get(0, 0) * obsProbs3.get(1, 0)
                * transProbs.get(0, 1) * obsProbs3.get(2, 1);
        assertTrue(equals(expectedProb, prob, EPSILON));
        prob = analyser.jointProbability(stateSequence3, obsProbs3, SequenceType.Complete);
        /*
         * Expected value:
         * P(<s_1=0,s_2=0,s_3=1>, <x_1,x_2,x_3>)
         * = P(<s_1=0,s_2=0,s_3=1], <x_1,x_2,x_3]) P(>|s_3=1).
         */
        System.out.printf("p(<s_1,...,s_T>)=%f%n", prob);
        expectedProb *= endProbs.get(1);
        assertTrue(equals(expectedProb, prob, EPSILON));
    }

    @Test
    public void testAnalyserObs() {
        System.out.println("testAnalyserObs:");
        MarkovOneStepAnalyser analyser = getAnalyser();
        double obsProb = analyser.dataProbability(obsProbs3, SequenceType.Complete);
        System.out.printf("p(<x_1,...,x_T>)=%f%n", obsProb);
        double expectedProb = 0.0;
        for (int s1 = 0; s1 <= 1; s1++) {
            for (int s2 = 0; s2 <= 1; s2++) {
                for (int s3 = 0; s3 <= 1; s3++) {
                    IndexVector stateSeq = VectorFactory.newIndexVector(s1, s2, s3);
                    double jointProb = analyser.jointProbability(stateSeq, obsProbs3, SequenceType.Complete);
                    VectorFactory.display("{s_t}=", stateSeq, String.format(", p({s_t}, {x_t})=%f%n", jointProb));
                    expectedProb += jointProb;
                }
            }
        }
        assertTrue(equals(expectedProb, obsProb, EPSILON));
    }

    @Test
    public void testAnalyserPredStates() {
        System.out.println("testAnalyserPredSates:");
        MarkovOneStepAnalyser analyser = getAnalyser();
        IndexVector obsSeq = analyser.stateSequence(obsProbs3, SequenceType.Complete);
        VectorFactory.display("argmax P(<s_1,...,s_T>|<x_1,...,x_T>)=", obsSeq, "\n");
        double expectedProb = 0.0;
        IndexVector expectedSeq = null;
        for (int s1 = 0; s1 <= 1; s1++) {
            for (int s2 = 0; s2 <= 1; s2++) {
                for (int s3 = 0; s3 <= 1; s3++) {
                    IndexVector stateSeq = VectorFactory.newIndexVector(s1, s2, s3);
                    double jointProb = analyser.jointProbability(stateSeq, obsProbs3, SequenceType.Complete);
                    if (jointProb > expectedProb) {
                        expectedProb = jointProb;
                        expectedSeq = stateSeq;
                    }
                }
            }
        }
        assertTrue(VectorFactory.equals(expectedSeq, obsSeq));
    }

}
