package gaj.afl.data;


public class MutableScore implements Score {

	private int numGoals = 0;
	private int numBehinds = 0;

	/**
	 * Initialises a mutable score with zero goals and behinds.
	 */
	public MutableScore() {}

	/**
	 * Initialises a mutable score from the given score.
	 * 
	 * @param score - The given score.
	 */
	public MutableScore(Score score) {
		numGoals = score.numGoals();
		numBehinds = score.numBehinds();
	}

	/**
	 * Initialises a mutable score with the given numbers of goals and behinds.
	 * 
	 * @param numGoals - The number of goals.
	 * @param numBehinds - The number of behinds.
	 */
	public MutableScore(int numGoals, int numBehinds) {
		this.numGoals = numGoals;
		this.numBehinds = numBehinds;
	}

	/**
	 * Adds the given score to the current score.
	 * 
	 * @param score - The incremental score.
	 */
	public void add(Score score) {
		numGoals += score.numGoals();
		numBehinds += score.numBehinds();
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
