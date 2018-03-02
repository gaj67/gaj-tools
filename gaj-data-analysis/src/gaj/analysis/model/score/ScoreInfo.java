package gaj.analysis.model.score;

/**
 * Gives a numerical score.
 */
public interface ScoreInfo {

    /**
     * A placeholder to use when a score is required but no score can be
     * computed.
     */
    final static ScoreInfo NO_SCORE_INFO = newScoreInfo(0, 0);

    /**
     * Creates immutable score information.
     * 
     * @param score
     *            - The unweighted score.
     * @param weight
     *            - The weight of the score.
     * @return The score object.
     */
    static ScoreInfo newScoreInfo(double score, double weight) {
        return new ScoreInfo() {
            @Override
            public double getScore() {
                return score;
            }

            @Override
            public double getWeight() {
                return weight;
            }
        };
    }

    /**
     * Obtains the numerical, unweighted score.
     * 
     * @return The score value.
     */
    double getScore();

    /**
     * Specifies the weight attached to the unweighted score.
     * 
     * @return The score weight.
     */
    double getWeight();

}
