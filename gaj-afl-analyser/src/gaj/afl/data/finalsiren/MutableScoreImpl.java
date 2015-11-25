package gaj.afl.data.finalsiren;

import gaj.afl.data.core.MutableScore;
import gaj.afl.data.core.Score;

public class MutableScoreImpl implements MutableScore {

    private int numGoals = 0;
    private int numBehinds = 0;

    /**
     * Initialises a mutable score with zero goals and behinds.
     */
    public MutableScoreImpl() {
    }

    /**
     * Initialises a mutable score from the given score.
     * 
     * @param score - The given score.
     */
    public MutableScoreImpl(Score score) {
        numGoals = score.numGoals();
        numBehinds = score.numBehinds();
    }

    /**
     * Initialises a mutable score with the given numbers of goals and behinds.
     * 
     * @param numGoals - The number of goals.
     * @param numBehinds - The number of behinds.
     */
    public MutableScoreImpl(int numGoals, int numBehinds) {
        this.numGoals = numGoals;
        this.numBehinds = numBehinds;
    }

    @Override
    public void add(Score score) {
        numGoals += score.numGoals();
        numBehinds += score.numBehinds();
    }

    @Override
    public void add(int numGoals, int numBehinds) {
        numGoals += numGoals;
        numBehinds += numBehinds;
    }

    @Override
    public int numGoals() {
        return numGoals;
    }

    @Override
    public int numBehinds() {
        return numBehinds;
    }

    @Override
    public int numPoints() {
        return 6 * numGoals + numBehinds;
    }

}
