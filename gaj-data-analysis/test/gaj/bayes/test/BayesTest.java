package gaj.bayes.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import gaj.analysis.bayes.pmf.EmpiricalIndexPMF;
import gaj.analysis.bayes.pmf.EmpiricalPMF;
import gaj.analysis.bayes.pmf.impl.integer.PMFFactory;

public class BayesTest {

    @Test
    public void testMAndMExampleV1() {
        EmpiricalIndexPMF<Integer> pmf = PMFFactory.newPMF(2);
        pmf.normalise();
        assertEquals(0.5, pmf.prob(0), 1e-10);
        assertEquals(0.5, pmf.prob(1), 1e-10);
        pmf.mult(0, 0.75);
        pmf.mult(1, 0.5);
        pmf.normalise();
        assertEquals(0.6, pmf.prob(0), 1e-10);
        assertEquals(0.4, pmf.prob(1), 1e-10);
    }

    @Test
    public void testMAndMExampleV2() {
        final String BOWL_1 = "Bowl 1";
        final String BOWL_2 = "Bowl 2";
        EmpiricalPMF<String> pmf = PMFFactory.newPMF(BOWL_1, BOWL_2);
        pmf.normalise();
        assertEquals(0.5, pmf.prob(BOWL_1), 1e-10);
        assertEquals(0.5, pmf.prob(BOWL_2), 1e-10);
        pmf.mult(BOWL_1, 0.75);
        pmf.mult(BOWL_2, 0.5);
        pmf.normalise();
        assertEquals(0.6, pmf.prob(BOWL_1), 1e-10);
        assertEquals(0.4, pmf.prob(BOWL_2), 1e-10);

    }

}
