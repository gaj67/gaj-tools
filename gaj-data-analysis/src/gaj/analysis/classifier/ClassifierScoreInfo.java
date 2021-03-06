package gaj.analysis.classifier;

import gaj.analysis.model.score.ScoreInfo;
import gaj.analysis.numeric.vector.DataVector;

/**
 * Encapsulates the score of a classifier as computed by a dataScorer.
 */
public interface ClassifierScoreInfo extends ScoreInfo {

    /**
     * Obtains the score of the classifier on gold-standard data.
     * 
     * @return The classification score.
     */
    @Override
    double getScore();

    /**
     * Indicates whether or not the both the classifier and scorer are gradient-enabled,
     * i.e. if it is safe to call {@link #getGradient}().
     * 
     * @return A value of true (or false) if a score gradient is (or is not) obtainable.
     */
    boolean hasGradient();

    /**
     * Obtains the gradient of the classification score with respect to the classifier parameters.
     * 
     * @return The score gradient.
     * @throws IllegalStateException If the gradient cannot be computed.
     */
    DataVector getGradient();

}
