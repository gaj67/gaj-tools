package gaj.analysis.model.score;

/**
 * Indicates a {@link ScoreInfo} that has a specified weight. Typically this
 * would be used for scoring a {@link DataCase}.
 */
public interface WeightedScoreInfo extends ScoreInfo {

    /**
     * Specifies the weight attached to the unweighted score.
     * 
     * @return The score weight.
     */
    double getWeight();

}
