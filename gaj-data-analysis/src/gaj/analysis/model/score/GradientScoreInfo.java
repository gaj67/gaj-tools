package gaj.analysis.model.score;

import gaj.analysis.data.numeric.vector.DataVector;
import gaj.analysis.model.GradientAware;

/**
 * Indicates a score with additional gradient information.
 */
public interface GradientScoreInfo extends ScoreInfo, GradientAware {

    /**
     * Obtains the gradient of the unweighted score with respect to the model
     * parameters.
     * 
     * @return The score gradient.
     */
    DataVector getScoreGradient();

    /**
     * Creates immutable gradient score information.
     * 
     * @param score
     *            - The unweighted score.
     * @param weight
     *            - The weight of the score and the gradient.
     * @param gradient
     *            - The unweighted gradient.
     * @return The score object.
     */
    static ScoreInfo newScoreInfo(double score, double weight, DataVector gradient) {
        return new GradientScoreInfo() {
            @Override
            public double getWeight() {
                return weight;
            }

            @Override
            public double getScore() {
                return score;
            }

            @Override
            public DataVector getScoreGradient() {
                return gradient;
            }
        };
    }

}
